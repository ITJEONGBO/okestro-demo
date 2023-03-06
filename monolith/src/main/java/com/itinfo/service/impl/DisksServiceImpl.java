package com.itinfo.service.impl;

import com.itinfo.SystemServiceHelper;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.WebsocketService;
import com.itinfo.model.DiskCreateVo;
import com.itinfo.model.DiskMigrationVo;
import com.itinfo.model.DiskVo;
import com.itinfo.model.MessageVo;
import com.itinfo.service.DisksService;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.ovirt.engine.sdk4.builders.HostBuilder;
import org.ovirt.engine.sdk4.builders.HostStorageBuilder;
import org.ovirt.engine.sdk4.builders.LogicalUnitBuilder;
import org.ovirt.engine.sdk4.internal.containers.ImageContainer;
import org.ovirt.engine.sdk4.internal.containers.ImageTransferContainer;
import org.ovirt.engine.sdk4.services.DiskService;
import org.ovirt.engine.sdk4.services.HostStorageService;
import org.ovirt.engine.sdk4.services.HostsService;
import org.ovirt.engine.sdk4.services.ImageTransferService;
import org.ovirt.engine.sdk4.services.ImageTransfersService;
import org.ovirt.engine.sdk4.services.StorageDomainService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.services.VmsService;
import org.ovirt.engine.sdk4.types.Disk;
import org.ovirt.engine.sdk4.types.DiskAttachment;
import org.ovirt.engine.sdk4.types.DiskFormat;
import org.ovirt.engine.sdk4.types.DiskProfile;
import org.ovirt.engine.sdk4.types.DiskStatus;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.HostStorage;
import org.ovirt.engine.sdk4.types.Image;
import org.ovirt.engine.sdk4.types.ImageTransfer;
import org.ovirt.engine.sdk4.types.ImageTransferPhase;
import org.ovirt.engine.sdk4.types.LogicalUnit;
import org.ovirt.engine.sdk4.types.StorageDomain;
import org.ovirt.engine.sdk4.types.StorageType;
import org.ovirt.engine.sdk4.types.Vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DisksServiceImpl implements DisksService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private WebsocketService websocketService;
	@Autowired private ConnectionService connectionService;

	@Override
	public List<DiskVo> retrieveDisks() {
		Connection connection = adminConnectionService.getConnection();
		List<Disk> disks
				= SystemServiceHelper.getInstance().findAllDisks(connection, "");
		List<Vm> vms
				= SystemServiceHelper.getInstance().findAllVms(connection, "");

		Map<String, String> vmDiskIdMap = new HashMap<>();
		vms.forEach(vm -> {
			List<DiskAttachment> diskAttachments
					= SystemServiceHelper.getInstance().findDiskAttachmentsFromVm(connection, vm.id());
			// TODO: DiskAttach처리 필요
			diskAttachments.forEach(att -> {
				// vmDiskIdMap.put(vm.id(), diskAttachments)
			});
		});

		List<DiskVo> diskVoList = new ArrayList<>();
		for (Disk disk : disks) {
			DiskVo diskVo = new DiskVo();
			diskVo.setId(disk.id());
			diskVo.setName(disk.name());
			diskVo.setDescription(disk.description());
			diskVo.setType(disk.storageType().name());
			diskVo.setSharable(disk.shareable());
			if (disk.provisionedSize() != null) {
				diskVo.setVirtualSize(String.valueOf(disk.provisionedSize()));
			} else if (disk.provisionedSize() == null && disk.storageType().name().equals("LUN")) {
				diskVo.setVirtualSize(String.valueOf((disk.lunStorage().logicalUnits().get(0)).size()));
			}
			if (disk.status() != null) {
				diskVo.setStatus(disk.status().value());
			} else if (disk.status() == null) {
				diskVo.setStatus("ok");
			}
			if (disk.storageDomains() != null && disk.storageDomains().size() > 0) {
				diskVo.setStorageDomainId(disk.storageDomains().get(0).id());
				String storageDomainName = SystemServiceHelper.getInstance().findStorageDomain(connection, diskVo.getStorageDomainId()).name();
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
		Connection connection = this.adminConnectionService.getConnection();

		List<Disk> disks
				= SystemServiceHelper.getInstance().findAllDisks(connection, " Storage=" + storageDomainName);
		List<Vm> vms
				= SystemServiceHelper.getInstance().findAllVms(connection, "");

		Map<String, String> vmDiskIdMap = new HashMap<>();
		vms.forEach(vm -> {
			List<DiskAttachment> diskAttachments =
					SystemServiceHelper.getInstance().findDiskAttachmentsFromVm(connection, vm.id());
			// TODO: DiskAttach처리 필요
			diskAttachments.forEach(att -> {
				// vmDiskIdMap.put(vm.id(), diskAttachments)
			});
		});

		List<DiskVo> diskVoList = new ArrayList<>();
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
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		StorageDomain storageDomain
				= systemService.storageDomainsService().storageDomainService(diskCreateVo.getStorageDomainId()).get().send().storageDomain();
		List<DiskProfile> diskProfiles
				= systemService.diskProfilesService().list().send().profile();
		DiskProfile diskProfile
				= systemService.diskProfilesService().diskProfileService(diskCreateVo.getDiskProfileId()).get().send().profile();
		org.ovirt.engine.sdk4.services.DisksService disksService
				= systemService.disksService();

		Disk disk = null;
		MessageVo message = null;
		try {
			DiskBuilder diskBuilder = new DiskBuilder();
			diskBuilder.name(diskCreateVo.getName());
			diskBuilder.description(diskCreateVo.getDescription());
			if (diskCreateVo.getShareable()) {
				diskBuilder.format(DiskFormat.RAW);
			} else {
				diskBuilder.format(DiskFormat.COW);
			}
			diskBuilder.shareable(diskCreateVo.getShareable());
			diskBuilder.wipeAfterDelete(diskCreateVo.getWipeAfterDelete());
			diskBuilder.provisionedSize(BigInteger.valueOf(Integer.parseInt(diskCreateVo.getSize())).multiply(BigInteger.valueOf(2L).pow(30)));
			diskBuilder.storageDomains(new StorageDomain[] { storageDomain });
			disk = disksService.add().disk(diskBuilder).send().disk();
			DiskService diskService = disksService.diskService(disk.id());
			do {
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) { e.printStackTrace(); }
				disk = diskService.get().send().disk();
			} while (disk.status() != DiskStatus.OK);
			message = new MessageVo(
					"디스크 생성",
					"디스크 생성 완료("+ disk.name() + ")",
					"success"
			);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			message = new MessageVo(
					"디스크 생성",
					"디스크 생성 실패("+ diskCreateVo.getName() + ")",
					"error"
			);
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/disks/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void createLunDisk(DiskCreateVo diskCreateVo) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		List<HostStorage> luns
				= systemService.hostsService().hostService(diskCreateVo.getHostId()).storageService().list().send().storages();
		List<Host> hosts
				= systemService.hostsService().list().send().hosts();
		org.ovirt.engine.sdk4.services.DisksService disksService
				= systemService.disksService();
		MessageVo message;
		Disk disk = null;
		try {
			DiskBuilder diskBuilder = new DiskBuilder();
			diskBuilder.alias(diskCreateVo.getName());
			diskBuilder.description(diskCreateVo.getDescription());
			diskBuilder.shareable(diskCreateVo.getShareable());
			HostStorageBuilder lunStorage = new HostStorageBuilder();
			for (HostStorage lun : luns) {
				if (lun.id().equals(diskCreateVo.getLunId())) {
					LogicalUnitBuilder logicalUnitBuilder = new LogicalUnitBuilder();
					logicalUnitBuilder.id(diskCreateVo.getLunId());
					logicalUnitBuilder.lunMapping(((LogicalUnit)lun.logicalUnits().get(0)).lunMapping());
					logicalUnitBuilder.productId(((LogicalUnit)lun.logicalUnits().get(0)).productId());
					logicalUnitBuilder.serial(((LogicalUnit)lun.logicalUnits().get(0)).serial());
					logicalUnitBuilder.size(((LogicalUnit)lun.logicalUnits().get(0)).size());
					logicalUnitBuilder.vendorId(((LogicalUnit)lun.logicalUnits().get(0)).vendorId());
					HostBuilder hostBuilder = new HostBuilder();
					hostBuilder.id(diskCreateVo.getHostId());
					lunStorage.host(hostBuilder);
					lunStorage.type(StorageType.FCP);
					lunStorage.logicalUnits(new LogicalUnitBuilder[] { logicalUnitBuilder });
					diskBuilder.lunStorage(lunStorage);
					break;
				}
			}
			disk = disksService.add().disk(diskBuilder).send().disk();
			DiskService diskService
					= disksService.diskService(disk.id());
			message = new MessageVo(
					"디스크 생성",
					"디스크 생성 완료("+ disk.name() +")",
					"success"
			);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			message = new MessageVo(
					"디스크 생성",
					"디스크 생성 실패("+ diskCreateVo.getName() +")",
					"error"
			);
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/disks/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void removeDisk(List<String> disks) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		MessageVo message;
		String diskName = "";
		try {
			DiskService diskService = systemService.disksService().diskService(disks.get(0));
			Disk disk
					= diskService.get().send().disk();
			diskName
					= disk.name();
			diskService.remove().send();
			message = new MessageVo(
					"디스크 삭제",
					"디스크 삭제 완료("+ diskName + ")",
					"success"
			);
		} catch (Exception e) {
			e.printStackTrace();
			message = new MessageVo(
					"디스크 삭제",
					"디스크 삭제 실패("+ diskName + ")",
					"error"
			);
		}
		try {
			Thread.sleep(1000L);
		} catch (Exception exception) {}
		websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		websocketService.sendMessage("/topic/disks/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void migrationDisk(DiskMigrationVo diskMigrationVo) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		String actionName = "";
		if ("move".equalsIgnoreCase(diskMigrationVo.getMigrationType())) {
			actionName = "디스크 이동";
		} else if ("copy".equalsIgnoreCase(diskMigrationVo.getMigrationType())) {
			actionName = "디스크 복사";
		}
		MessageVo message;
		Disk disk = null;
		try {
			DiskService diskService
					= systemService.disksService().diskService(diskMigrationVo.getDisk().getId());
			StorageDomain target
					= systemService.storageDomainsService().storageDomainService(diskMigrationVo.getTargetStorageDomainId()).get().send().storageDomain();
			if ("move".equalsIgnoreCase(diskMigrationVo.getMigrationType())) {
				DiskService.MoveResponse moveResponse = (DiskService.MoveResponse)diskService.move().storageDomain(target).send();
			} else if ("copy".equalsIgnoreCase(diskMigrationVo.getMigrationType())) {
				diskService.copy()
						.disk((new DiskBuilder()).name(diskMigrationVo.getTargetDiskName()))
						.storageDomain(target)
						.send();
			}
			do {
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) { e.printStackTrace(); }
				disk = diskService.get().send().disk();
			} while (disk.status() != DiskStatus.OK);
			message = new MessageVo(
					actionName,
					actionName + " 완료(" + disk.name() + ")",
					"success"
			);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			message = new MessageVo(
					actionName,
					actionName + " 실패(" + disk.name() + ")",
					"error"
			);
		}
		try {
			Thread.sleep(1000L);
		} catch (Exception exception) {}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/disks/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void uploadDisk(byte[] bytes, DiskCreateVo diskCreateVo, InputStream is, long diskSize) {
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
				if (diskCreateVo.getShareable()) {
					diskBuilder.format(DiskFormat.RAW);
				} else {
					diskBuilder.format(DiskFormat.COW);
				}
			} else if (!"qcow2".equals(diskCreateVo.getFormat())) {
				diskBuilder.format(DiskFormat.RAW);
			}
			diskBuilder.shareable(diskCreateVo.getShareable());
			diskBuilder.wipeAfterDelete(diskCreateVo.getWipeAfterDelete());
			if (!"qcow2".equals(diskCreateVo.getFormat())) {
				diskBuilder.provisionedSize(diskSize);
			} else if ("qcow2".equals(diskCreateVo.getFormat())) {
				diskBuilder.provisionedSize(diskCreateVo.getVirtualSize());
			}
			diskBuilder.storageDomains(new StorageDomain[] { storageDomain });
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
			transfer2.image((Image)image);
			ImageTransfer transfer = ((ImageTransfersService.AddResponse)transfersService.add().imageTransfer((ImageTransfer)transfer2).send()).imageTransfer();
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
						if (responseCode == 200) {
							message.setTitle(disk.id());
							message.setText(String.format("완료", new Object[0]));
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
		return null;

	}

	public static void setTrustStore() throws Exception {
		String path = System.getProperty("user.dir");
		log.info("path는 ... "+ path);
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		InputStream is = new FileInputStream("../webapps/ROOT/WEB-INF/cert.pem");
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
			String buf = new String();
			String resultBuf = new String();
			while ((buf = bufferedReader.readLine()) != null)
				resultBuf = resultBuf + buf;
			resultString = resultBuf;
			log.info("##################################");
			log.info("res chk     :" + resultString);
			bufferedReader.close();
		} catch (MalformedURLException | KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return "timeout";
		} finally {
			try {
				if (bufferedReader != null)	bufferedReader.close();
				if (inputStream != null)	inputStream.close();
				if (outputStream != null)	outputStream.close();
				if (conn != null)			conn.disconnect();
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}
		Thread.sleep(2000L);
		return resultString;
	}
}
