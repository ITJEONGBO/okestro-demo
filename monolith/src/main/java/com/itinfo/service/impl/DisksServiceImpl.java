package com.itinfo.service.impl;

import com.itinfo.ItInfoConstant;
import com.itinfo.common.CloseableExtKt;
import com.itinfo.model.*;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.WebsocketService;
import com.itinfo.service.DisksService;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.DiskBuilder;
import org.ovirt.engine.sdk4.internal.containers.ImageContainer;
import org.ovirt.engine.sdk4.internal.containers.ImageTransferContainer;
import org.ovirt.engine.sdk4.services.ImageTransferService;
import org.ovirt.engine.sdk4.services.ImageTransfersService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.Disk;
import org.ovirt.engine.sdk4.types.DiskAttachment;
import org.ovirt.engine.sdk4.types.DiskFormat;
import org.ovirt.engine.sdk4.types.DiskStatus;
import org.ovirt.engine.sdk4.types.ImageTransfer;
import org.ovirt.engine.sdk4.types.ImageTransferPhase;
import org.ovirt.engine.sdk4.types.StorageDomain;
import org.ovirt.engine.sdk4.types.Vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DisksServiceImpl extends BaseService implements DisksService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private WebsocketService websocketService;
	@Autowired private ConnectionService connectionService;

	@Override
	public List<DiskVo> retrieveDisks() {
		log.info("... retrieveDisks");
		Connection connection = adminConnectionService.getConnection();
		List<Disk> disks
				= getSysSrvHelper().findAllDisks(connection, "");
		List<Vm> vms
				= getSysSrvHelper().findAllVms(connection, "");
		Map<String, String> vmDiskIdMap = new HashMap<>();
		vms.forEach(vm -> {
			List<DiskAttachment> diskAttachments =
					getSysSrvHelper().findAllDiskAttachmentsFromVm(connection, vm.id());
			diskAttachments.forEach(att -> {
				vmDiskIdMap.put(att.id(), vm.name());
			});
		});

		List<DiskVo> diskVoList
				= ModelsKt.toDiskVos(disks, connection, null);

		for (Disk disk : disks) {
			DiskVo diskVo = new DiskVo();
			diskVo.setId(disk.id());
			diskVo.setName(disk.name());
			diskVo.setDescription(disk.description());
			diskVo.setType(disk.storageType().name());
			diskVo.setSharable(disk.shareable());
			if (disk.provisionedSizePresent()) {
				diskVo.setVirtualSize(String.valueOf(disk.provisionedSize()));
			} else if (disk.provisionedSize() == null && disk.storageType().name().equals("LUN")) {
				diskVo.setVirtualSize(String.valueOf((disk.lunStorage().logicalUnits().get(0)).size()));
			}
			diskVo.setStatus(disk.statusPresent() ? disk.status().value() : "ok");
			if (disk.storageDomains() != null && disk.storageDomains().size() > 0) {
				diskVo.setStorageDomainId(disk.storageDomains().get(0).id());
				String storageDomainName = getSysSrvHelper().findStorageDomain(connection, diskVo.getStorageDomainId()).name();
				diskVo.setStorageDomainName(storageDomainName);
			}

			if (vmDiskIdMap.containsKey(disk.id()))
				diskVo.setAttachedTo(vmDiskIdMap.get(disk.id()));
			diskVoList.add(diskVo);
		}
		return diskVoList;
	}

	@Override
	public List<DiskVo> retrieveDisks(String storageDomainName) {
		log.info("... retrieveDisks('{}')", storageDomainName);
		Connection connection = this.adminConnectionService.getConnection();
		List<Disk> disks
				= getSysSrvHelper().findAllDisks(connection, " Storage=" + storageDomainName);
		List<Vm> vms
				= getSysSrvHelper().findAllVms(connection, "");

		Map<String, String> vmDiskIdMap = new HashMap<>();
		vms.forEach(vm -> {
			List<DiskAttachment> diskAttachments =
					getSysSrvHelper().findAllDiskAttachmentsFromVm(connection, vm.id());
			diskAttachments.forEach(att -> {
				vmDiskIdMap.put(att.id(), vm.name());
			});
		});

		List<DiskVo> diskVoList
				= ModelsKt.toDiskVos(disks, connection, new ArrayList<>());

		for (Disk disk : disks) {
			DiskVo diskVo = new DiskVo();
			diskVo.setId(disk.id());
			diskVo.setName(disk.name());
			diskVo.setDescription(disk.description());
			diskVo.setType(disk.storageType().name());
			diskVo.setSharable(disk.shareable());
			diskVo.setActualSize(String.valueOf(disk.actualSize()));
			diskVo.setVirtualSize(String.valueOf(disk.provisionedSize()));
			diskVo.setStatus(disk.status().value());
			if (vmDiskIdMap.containsKey(disk.id()))
				diskVo.setAttachedTo(vmDiskIdMap.get(disk.id()));
			diskVoList.add(diskVo);
		}
		return diskVoList;
	}

	@Override
	public void createDisk(DiskCreateVo diskCreateVo) {
		log.info("... createDisk");
		Connection connection = this.adminConnectionService.getConnection();
		Disk disk;
		MessageVo message;
		try {
			/*
			DiskBuilder diskBuilder = new DiskBuilder()
					.name(diskCreateVo.getName())
					.description(diskCreateVo.getDescription())
					.format(diskCreateVo.getShareable() ? DiskFormat.RAW : DiskFormat.COW)
					.shareable(diskCreateVo.getShareable())
					.wipeAfterDelete(diskCreateVo.getWipeAfterDelete())
					.provisionedSize(BigInteger.valueOf(Integer.parseInt(diskCreateVo.getSize())).multiply(BigInteger.valueOf(2L).pow(30)))
					.storageDomains(storageDomain);
			*/
			disk
					= getSysSrvHelper().addDisk(connection, ModelsKt.toDisk(diskCreateVo, connection));
			if (disk != null) {
				do {
					try { Thread.sleep(5000L); } catch (InterruptedException e) { log.error(e.getLocalizedMessage()); }
					if (disk.idPresent()) disk = getSysSrvHelper().findDisk(connection, disk.id());
				} while (disk.status() != DiskStatus.OK);
			}
			message
					= MessageVo.createMessage(MessageType.DISK_ADD, true, diskCreateVo.getName(), "");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			message
					= MessageVo.createMessage(MessageType.DISK_ADD, false, diskCreateVo.getName(), e.getLocalizedMessage());
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/disks/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void createLunDisk(DiskCreateVo diskCreateVo) {
		log.info("... createLunDisk");
		Connection connection = this.adminConnectionService.getConnection();
		/*
		SystemService systemService = connection.systemService();
		List<HostStorage> luns
				= systemService.hostsService().hostService(diskCreateVo.getHostId()).storageService().list().send().storages();
		List<Host> hosts
				= systemService.hostsService().list().send().hosts();
		*/
		MessageVo message = null;
		Disk disk;
		try {
			/*
			DiskBuilder diskBuilder = new DiskBuilder()
				.alias(diskCreateVo.getName())
				.description(diskCreateVo.getDescription())
				.shareable(diskCreateVo.getShareable());
			HostStorageBuilder lunStorage = new HostStorageBuilder();
			for (HostStorage lun : luns) {
				if (lun.id().equals(diskCreateVo.getLunId())) {
					LogicalUnitBuilder logicalUnitBuilder = new LogicalUnitBuilder()
						.id(diskCreateVo.getLunId())
						.lunMapping((lun.logicalUnits().get(0)).lunMapping())
						.productId((lun.logicalUnits().get(0)).productId())
						.serial((lun.logicalUnits().get(0)).serial())
						.size((lun.logicalUnits().get(0)).size())
						.vendorId((lun.logicalUnits().get(0)).vendorId());

					HostBuilder hostBuilder = new HostBuilder()
						.id(diskCreateVo.getHostId());

					lunStorage.host(hostBuilder);
					lunStorage.type(StorageType.FCP);
					lunStorage.logicalUnits(logicalUnitBuilder);
					diskBuilder.lunStorage(lunStorage);
					break;
				}
			}
			*/
			disk = getSysSrvHelper().addDisk(connection, ModelsKt.toDisk4Lun(diskCreateVo, connection));
			if (disk != null)
				message = MessageVo.createMessage(MessageType.DISK_ADD, true, disk.name(), "");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			message = MessageVo.createMessage(MessageType.DISK_ADD, false, diskCreateVo.getName(), e.getLocalizedMessage());
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/disks/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void removeDisk(List<String> diskIds) {
		log.info("... removeDisk");
		Connection connection = this.adminConnectionService.getConnection();
		MessageVo message;
		String diskName = "";
		for (String diskId: diskIds) {
			try {
				Disk disk = getSysSrvHelper().findDisk(connection, diskId);
				diskName = disk.name();
				boolean res = getSysSrvHelper().removeDisk(connection, diskId);
				message = MessageVo.createMessage(MessageType.DISK_REMOVE, res, diskName, "");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message = MessageVo.createMessage(MessageType.DISK_REMOVE, false, diskName, e.getLocalizedMessage());
			}
			try { Thread.sleep(1000L); } catch (InterruptedException e) { e.getLocalizedMessage(); }
			websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			websocketService.sendMessage("/topic/disks/reload", "");
		}
	}

	@Async("karajanTaskExecutor")
	@Override
	public void migrationDisk(DiskMigrationVo diskMigrationVo) {
		log.info("... migrationDisk");
		Connection connection = this.adminConnectionService.getConnection();
		if (!("move".equalsIgnoreCase(diskMigrationVo.getMigrationType()) ||
			"copy".equalsIgnoreCase(diskMigrationVo.getMigrationType()))) {
			log.error("처리 보류 ... 찾을 수 없는 디스크 처리 행위 ... migrationType: "+diskMigrationVo.getMigrationType());
			return;
		}
		MessageVo message;
		Disk disk = null;
		boolean res = false;

		try {
			StorageDomain target
					= getSysSrvHelper().findStorageDomain(connection, diskMigrationVo.getTargetStorageDomainId());
			if ("move".equalsIgnoreCase(diskMigrationVo.getMigrationType())) {
				res = getSysSrvHelper().moveDisk(connection, diskMigrationVo.getDisk().getId(), target);
			} else if ("copy".equalsIgnoreCase(diskMigrationVo.getMigrationType())) {
				Disk disk2copy = new DiskBuilder().name(diskMigrationVo.getTargetDiskName()).build();
				res = getSysSrvHelper().copyDisk(connection, diskMigrationVo.getDisk().getId(), disk2copy, target);
			}
			do {
				try { Thread.sleep(5000L); } catch (InterruptedException e) { log.error(e.getLocalizedMessage()); }
				disk = getSysSrvHelper().findDisk(connection, diskMigrationVo.getDisk().getId());
			} while (disk.status() != DiskStatus.OK);
			message = MessageVo.createMessage(
						"move".equalsIgnoreCase(diskMigrationVo.getMigrationType()) ? MessageType.DISK_MOVE : MessageType.DISK_COPY,
						res, disk.name(), "");

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			message = MessageVo.createMessage(
					"move".equalsIgnoreCase(diskMigrationVo.getMigrationType()) ? MessageType.DISK_MOVE : MessageType.DISK_COPY,
					res, disk.name(), e.getLocalizedMessage());
		}
		try { Thread.sleep(1000L); } catch (InterruptedException e) { e.getLocalizedMessage(); }
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/disks/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void uploadDisk(byte[] bytes, DiskCreateVo diskCreateVo, InputStream is, long diskSize) {
		log.info("... uploadDisk");
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		StorageDomain storageDomain
				= systemService.storageDomainsService().storageDomainService(diskCreateVo.getStorageDomainId()).get().send().storageDomain();
		org.ovirt.engine.sdk4.services.DisksService disksService
				= systemService.disksService();
		MessageVo message = new MessageVo();
		try {
			DiskBuilder diskBuilder = new DiskBuilder();
			diskBuilder.name(diskCreateVo.getName());
			diskBuilder.description(diskCreateVo.getDescription());
			if ("qcow2".equals(diskCreateVo.getFormat())) {
				diskBuilder.format(diskCreateVo.getShareable() ? DiskFormat.RAW : DiskFormat.COW);
			} else {
				diskBuilder.format(DiskFormat.RAW);
			}
			diskBuilder.shareable(diskCreateVo.getShareable());
			diskBuilder.wipeAfterDelete(diskCreateVo.getWipeAfterDelete());
			if (!"qcow2".equals(diskCreateVo.getFormat())) {
				diskBuilder.provisionedSize(diskSize);
			} else if ("qcow2".equals(diskCreateVo.getFormat())) {
				diskBuilder.provisionedSize(diskCreateVo.getVirtualSize());
			}
			diskBuilder.storageDomains(storageDomain);
			Disk disk = disksService.add().disk(diskBuilder).send().disk();
			do {
				Thread.sleep(3000L);
			} while (disksService.diskService(disk.id()).get().send().disk().status() != DiskStatus.OK);
			message.setTitle(disk.id());
			message.setText("업로드 준비 중 ...");
			this.websocketService.sendMessage("/topic/disks/uploadDisk", (new Gson()).toJson(message));
			ImageTransfersService transfersService = systemService.imageTransfersService();
			ImageTransferContainer transfer2 = new ImageTransferContainer();
			ImageContainer image = new ImageContainer();
			image.id(disk.id());
			transfer2.image(image);
			ImageTransfer transfer = (transfersService.add().imageTransfer(transfer2).send()).imageTransfer();
			while (true) {
				Thread.sleep(1000L);
				if (transfer.phase() != ImageTransferPhase.INITIALIZING) {
					transfersService.imageTransferService(transfer.id()).get();
					if (transfer.transferUrl() != null) {
						URL url = new URL(transfer.transferUrl());
						requestVfrontConnection(url, bytes);
						setTrustStore();
						System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
						HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
						https.setRequestProperty("PUT", url.getPath());
						long length = diskSize;
						log.info("length = " + length);
						https.setRequestProperty("Content-Length", String.valueOf(length));
						https.setRequestMethod("PUT");
						https.setFixedLengthStreamingMode(diskSize);
						https.setDoOutput(true);
						https.connect();
						Thread.sleep(2000L);
						OutputStream os = https.getOutputStream();
						byte[] buf = new byte[131072];
						long read = 0L;
						DecimalFormat form = new DecimalFormat("#.#");
						message.setTitle(disk.id());
						message.setText("업로드 중 ...");
						do {
							int readNow = is.read(buf);
							os.write(buf, 0, readNow);
							os.flush();
							read += readNow;
							if (Math.floor(read * 100.0D / length) != Double.parseDouble(form.format(read * 100.0D / length)))
								continue;
							message.setTitle(disk.id());
							message.setText((int)(read * 100.0D / length) + "%");
							this.websocketService.sendMessage("/topic/disks/uploadDisk", (new Gson()).toJson(message));
						} while (read < length);
						int responseCode = https.getResponseCode();
						if (responseCode == ItInfoConstant.STATUS_OK) {
							message.setTitle(disk.id());
							message.setText("완료");
							this.websocketService.sendMessage("/topic/disks/uploadDisk", (new Gson()).toJson(message));
						}
						is.close();
						os.close();
						ImageTransferService transferService = systemService.imageTransfersService().imageTransferService(transfer.id());
						transferService.finalize_().send();
						https.disconnect();
						connection.close();
						message.setTitle("디스크 업로드");
						message.setText(diskCreateVo.getName() + "디스크 업로드 완료");
						message.setStyle("success");
					}
				} else {
					continue;
				}
				this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
				this.websocketService.sendMessage("/topic/disks/reload", "");
				return;
			}
		} catch (Exception e) {
			e.fillInStackTrace();
			log.debug(e.getLocalizedMessage());
			message.setTitle("디스크 업로드");
			message.setText(diskCreateVo.getName() + "디스크 업로드 실패");
			message.setStyle("error");
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/disks/reload", "");
	}

	@Override
	public String retrieveDiskImage(File file) throws IOException {
		log.info("... retrieveDiskImage");
		return null;
	}

	public static void setTrustStore() throws Exception {
		log.info("... setTrustStore");
		String path = System.getProperty("user.dir");
		log.info("path는 ... "+ path);
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		InputStream is = Files.newInputStream(Paths.get("../webapps/ROOT/WEB-INF/cert.pem"));

		MessageVo message = new MessageVo();
		message.setTitle("cert.pem");
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null);
		ks.setCertificateEntry("caCert", caCert);
		tmf.init(ks);
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tmf.getTrustManagers(), null);
		SSLContext.setDefault(sslContext);
	}

	public static String requestVfrontConnection(URL url, byte[] bytes) throws InterruptedException {
		log.info("... requestVfrontConnection('{}')", url);
		HttpsURLConnection conn = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		String resultString = null;
		SSLContext sslContext = null;
		try {
			HostnameVerifier hostnameVerifier = new HostnameVerifier() {
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			};
			TrustManager[] trustManager = { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
			} };
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustManager, new SecureRandom());
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			conn = (HttpsURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setConnectTimeout(2000);
			outputStream = conn.getOutputStream();
			outputStream.write(bytes[0]);
			outputStream.flush();
			outputStream.close();
			inputStream = conn.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String buf = "";
			String resultBuf = "";
			while ((buf = bufferedReader.readLine()) != null)
				resultBuf = resultBuf + buf;
			resultString = resultBuf;
			log.info("##################################");
			log.info("res chk     :" + resultString);
			bufferedReader.close();
		} catch (MalformedURLException | KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			return "timeout";
		} finally {
			CloseableExtKt.doCloseAll(Arrays.asList(bufferedReader, inputStream, outputStream));
			try {
				if (conn != null)			conn.disconnect();
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}
		Thread.sleep(2000L);
		return resultString;
	}
}
