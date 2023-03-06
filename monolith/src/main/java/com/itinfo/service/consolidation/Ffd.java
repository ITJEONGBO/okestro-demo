package com.itinfo.service.consolidation;

import com.itinfo.model.karajan.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Ffd {
	public List<ConsolidationVo> optimizeDataCenter(KarajanVo karajan, String clusterId) {
		List<ClusterVo> clusterInfo = karajan.getClusters();
		List<HostVo> afterHostInfo = new ArrayList<>();
		int i;
		for (i = 0; i < clusterInfo.size(); i++) {
			if (clusterId.equals(((ClusterVo)clusterInfo.get(i)).getId())) {
				afterHostInfo = ((ClusterVo)clusterInfo.get(i)).getHosts();
				break;
			}
		}
		for (i = afterHostInfo.size() - 1; i >= 0; i--) {
			if (!afterHostInfo.get(i).getStatus().equals("up"))
				afterHostInfo.remove(i);
		}
		List<ConsolidationVo> migrationScheduleInfo = new ArrayList<>();
		do {
			migrationScheduleInfo.clear();
			migrationScheduleInfo = consolidateVM(karajan, afterHostInfo);
			if (migrationScheduleInfo.size() == 0) continue;
			afterHostInfo = updateHostInfo(afterHostInfo, migrationScheduleInfo);
		} while (migrationScheduleInfo.size() > 0);
		return getMigrationSchedule(afterHostInfo);
	}

	public List<ConsolidationVo> consolidateVM(KarajanVo karajan, List<HostVo> hostInfo) {
		int clusterMemoryThreshold = karajan.getMemoryThreshold();
		List<ConsolidationVo> migrationScheduleInfo = new ArrayList<>();
		new ArrayList();
		List<VmVo> removedVmInfo = new ArrayList<>();
		AscendingHostComparator hostComparator = new AscendingHostComparator();
		Collections.sort(hostInfo, hostComparator);
		for (int i = 0; i < hostInfo.size() - 1; i++) {
			List<VmVo> vmInfo = clone(((HostVo)hostInfo.get(i)).getVms());
			if (vmInfo.size() >= 1)
				if (vmInfo.stream().allMatch(vx -> vx.getPlacementPolicy().equals("migratable"))) {
					AscendingVmComparator vmComparator = new AscendingVmComparator();
					Collections.sort(vmInfo, vmComparator);
					for (int v = vmInfo.size() - 1; v >= 0; v--) {
						for (int h = hostInfo.size() - 1; h >= 0; h--) {
							if (i == 2 && h == 0) {
								Boolean a = Boolean.valueOf(true);
								a = Boolean.valueOf(true);
							}
							BigInteger hostMax = ((HostVo)hostInfo.get(0)).getMaxSchedulingMemory();
							if (h != i && ((HostVo)hostInfo.get(h)).getVms().size() >= 1) {
								BigDecimal tempMemoryTotal = ((HostVo)hostInfo.get(h)).getMemoryTotal();
								BigDecimal tempMemoryUsed = ((HostVo)hostInfo.get(h)).getMemoryUsed();
								BigDecimal availableMemory = tempMemoryTotal.multiply(new BigDecimal(clusterMemoryThreshold / 100.0D));
								BigDecimal assignedMemory = tempMemoryUsed.add(((VmVo)vmInfo.get(v)).getMemoryInstalled());
								BigDecimal hostMaxSchedulingMemory = (new BigDecimal(((HostVo)hostInfo.get(h)).getMaxSchedulingMemory())).multiply(new BigDecimal(clusterMemoryThreshold / 100.0D));
								if (availableMemory.compareTo(assignedMemory) == 1 && hostMaxSchedulingMemory.compareTo(((VmVo)vmInfo.get(v)).getMemoryInstalled()) != -1) {
									int vmVCpu = ((VmVo)vmInfo.get(v)).getCores() * ((VmVo)vmInfo.get(v)).getSockets() * ((VmVo)vmInfo.get(v)).getThreads();
									int HostVCpu = ((HostVo)hostInfo.get(h)).getCores() * ((HostVo)hostInfo.get(h)).getSockets() * ((HostVo)hostInfo.get(h)).getThreads();
									if (HostVCpu >= vmVCpu + ((HostVo)hostInfo.get(h)).getCpuVmUsed()) {
										((HostVo)hostInfo.get(h)).setCpuVmUsed(vmVCpu + ((HostVo)hostInfo.get(h)).getCpuVmUsed());
										((HostVo)hostInfo.get(h)).setMemoryUsed(assignedMemory);
										((HostVo)hostInfo.get(h)).setMemoryFree(tempMemoryTotal.subtract(assignedMemory));
										((HostVo)hostInfo.get(h)).setMaxSchedulingMemory(((HostVo)hostInfo.get(h)).getMaxSchedulingMemory().subtract(((VmVo)vmInfo.get(v)).getMemoryInstalled().toBigInteger()));
										((HostVo)hostInfo.get(i)).setCpuVmUsed(vmVCpu - ((HostVo)hostInfo.get(i)).getCpuVmUsed());
										((HostVo)hostInfo.get(i)).setMemoryUsed(((HostVo)hostInfo.get(i)).getMemoryUsed().subtract(((VmVo)vmInfo.get(v)).getMemoryInstalled()));
										((HostVo)hostInfo.get(i)).setMemoryFree(((HostVo)hostInfo.get(i)).getMemoryTotal().subtract(((HostVo)hostInfo.get(i)).getMemoryFree()));
										((HostVo)hostInfo.get(i)).setMaxSchedulingMemory(((HostVo)hostInfo.get(i)).getMaxSchedulingMemory().add(((VmVo)vmInfo.get(v)).getMemoryInstalled().toBigInteger()));
										ConsolidationVo migrationInfo =
												KarajanModelsKt.toConsolidationVoWithSpecificHost(vmInfo.get(v), hostInfo.get(h));
										migrationScheduleInfo.add(migrationInfo);
										removedVmInfo.add(vmInfo.get(v));
										vmInfo.remove(v);
										break;
									}
								}
							}
						}
					}
					if (vmInfo.size() < 1)
						break;
					hostInfo = rollBackHostInfo(migrationScheduleInfo, hostInfo, removedVmInfo);
					migrationScheduleInfo.clear();
					removedVmInfo.clear();
				} else if (!vmInfo.stream().allMatch(vx -> vx.getPlacementPolicy().equals("migratable"))) {

				}
		}
		migrationScheduleInfo.size();
		return migrationScheduleInfo;
	}

	public List<ConsolidationVo> getMigrationSchedule(List<HostVo> hostInfo) {
		List<ConsolidationVo> migrationScheduleInfo = new ArrayList<>();
		for (int i = 0; i < hostInfo.size(); i++) {
			new ArrayList();
			List<VmVo> vmInfo = ((HostVo)hostInfo.get(i)).getVms();
			String hostId = ((HostVo)hostInfo.get(i)).getId();
			for (int j = 0; j < vmInfo.size(); j++) {
				String vmHostId = ((VmVo)vmInfo.get(j)).getHostId();
				if (!hostId.equals(vmHostId)) {
					ConsolidationVo migrationInfo =
							KarajanModelsKt.toConsolidationVoWithSpecificHost(vmInfo.get(j), hostInfo.get(i));
					migrationScheduleInfo.add(migrationInfo);
				}
			}
		}
		return migrationScheduleInfo;
	}

	public List<HostVo> updateHostInfo(List<HostVo> hostInfo, List<ConsolidationVo> migrationSchedule) {
		for (int i = 0; i < migrationSchedule.size(); i++) {
			String sourceHostId = ((ConsolidationVo)migrationSchedule.get(i)).getFromHostId();
			String migrationVmId = ((ConsolidationVo)migrationSchedule.get(i)).getVmId();
			String destinationHostId = ((ConsolidationVo)migrationSchedule.get(i)).getHostId();
			int sourceHostIndex = findHostIndex(hostInfo, sourceHostId);
			int destinationHostIndex = findHostIndex(hostInfo, destinationHostId);
			List<VmVo> sourceVm = clone(((HostVo)hostInfo.get(sourceHostIndex)).getVms());
			int migrationVmIndex = findVmIndex(sourceVm, migrationVmId);
			VmVo migrationVm = sourceVm.get(migrationVmIndex);
			List<VmVo> destinationVm = clone(((HostVo)hostInfo.get(destinationHostIndex)).getVms());
			destinationVm.add(sourceVm.get(migrationVmIndex));
			sourceVm.remove(migrationVmIndex);
			((HostVo)hostInfo.get(destinationHostIndex)).setVms(destinationVm);
			((HostVo)hostInfo.get(sourceHostIndex)).setVms(sourceVm);
			BigDecimal memoryUsed = ((HostVo)hostInfo.get(destinationHostIndex)).getMemoryUsed();
			BigDecimal memoryTotal = ((HostVo)hostInfo.get(destinationHostIndex)).getMemoryTotal();
			BigDecimal maxSchedulingMemory = new BigDecimal(((HostVo)hostInfo.get(destinationHostIndex)).getMaxSchedulingMemory());
			int cpuVmUsed = 0;
			List<VmVo> vmInfo = ((HostVo)hostInfo.get(destinationHostIndex)).getVms();
			int j;
			for (j = 0; j < vmInfo.size(); j++)
				cpuVmUsed += ((VmVo)vmInfo.get(j)).getCores() * ((VmVo)vmInfo.get(j)).getSockets() * ((VmVo)vmInfo.get(j)).getThreads();
			((HostVo)hostInfo.get(destinationHostIndex)).setMemoryUsed(memoryUsed.add(migrationVm.getMemoryInstalled()));
			((HostVo)hostInfo.get(destinationHostIndex)).setMemoryFree(memoryTotal.subtract(memoryUsed));
			((HostVo)hostInfo.get(destinationHostIndex)).setMaxSchedulingMemory(maxSchedulingMemory.subtract(migrationVm.getMemoryInstalled()).toBigInteger());
			((HostVo)hostInfo.get(destinationHostIndex)).setCpuVmUsed(cpuVmUsed);
			memoryUsed = ((HostVo)hostInfo.get(sourceHostIndex)).getMemoryUsed();
			memoryTotal = ((HostVo)hostInfo.get(sourceHostIndex)).getMemoryTotal();
			maxSchedulingMemory = new BigDecimal(((HostVo)hostInfo.get(sourceHostIndex)).getMaxSchedulingMemory());
			cpuVmUsed = 0;
			vmInfo = ((HostVo)hostInfo.get(sourceHostIndex)).getVms();
			for (j = 0; j < vmInfo.size(); j++)
				cpuVmUsed += ((VmVo)vmInfo.get(j)).getCores() * ((VmVo)vmInfo.get(j)).getSockets() * ((VmVo)vmInfo.get(j)).getThreads();
			((HostVo)hostInfo.get(sourceHostIndex)).setMemoryUsed(memoryUsed.subtract(migrationVm.getMemoryInstalled()));
			((HostVo)hostInfo.get(sourceHostIndex)).setMemoryFree(memoryTotal.subtract(memoryUsed));
			((HostVo)hostInfo.get(sourceHostIndex)).setMaxSchedulingMemory(maxSchedulingMemory.add(migrationVm.getMemoryInstalled()).toBigInteger());
			((HostVo)hostInfo.get(sourceHostIndex)).setCpuVmUsed(cpuVmUsed);
		}
		return hostInfo;
	}

	public List<HostVo> rollBackHostInfo(List<ConsolidationVo> migrationSchedule, List<HostVo> hostInfo, List<VmVo> vmInfo) {
		for (int i = 0; i < migrationSchedule.size(); i++) {
			String sourceHostId = ((ConsolidationVo)migrationSchedule.get(i)).getFromHostId();
			String destinationHostId = ((ConsolidationVo)migrationSchedule.get(i)).getHostId();
			String candidatedVmId = ((ConsolidationVo)migrationSchedule.get(i)).getVmId();
			int sourceHostIndex = findHostIndex(hostInfo, sourceHostId);
			int destinationHostIndex = findHostIndex(hostInfo, destinationHostId);
			int candidatedVmIndex = findVmIndex(vmInfo, candidatedVmId);
			BigDecimal vmMemoryInstalled = ((VmVo)vmInfo.get(candidatedVmIndex)).getMemoryInstalled();
			int cpuVmUsed = ((VmVo)vmInfo.get(candidatedVmIndex)).getCores() * ((VmVo)vmInfo.get(candidatedVmIndex)).getSockets() * ((VmVo)vmInfo.get(candidatedVmIndex)).getThreads();
			((HostVo)hostInfo.get(sourceHostIndex)).setMemoryUsed(((HostVo)hostInfo.get(sourceHostIndex)).getMemoryUsed().add(vmMemoryInstalled));
			((HostVo)hostInfo.get(sourceHostIndex)).setMemoryFree(((HostVo)hostInfo.get(sourceHostIndex)).getMemoryTotal().subtract(((HostVo)hostInfo.get(sourceHostIndex)).getMemoryUsed()));
			((HostVo)hostInfo.get(sourceHostIndex)).setMaxSchedulingMemory(((HostVo)hostInfo.get(sourceHostIndex)).getMaxSchedulingMemory().subtract(vmMemoryInstalled.toBigInteger()));
			((HostVo)hostInfo.get(sourceHostIndex)).setCpuVmUsed(((HostVo)hostInfo.get(sourceHostIndex)).getCpuVmUsed() + cpuVmUsed);
			((HostVo)hostInfo.get(destinationHostIndex)).setMemoryUsed(((HostVo)hostInfo.get(destinationHostIndex)).getMemoryUsed().subtract(vmMemoryInstalled));
			((HostVo)hostInfo.get(destinationHostIndex)).setMemoryFree(((HostVo)hostInfo.get(destinationHostIndex)).getMemoryTotal().subtract(((HostVo)hostInfo.get(destinationHostIndex)).getMemoryUsed()));
			((HostVo)hostInfo.get(destinationHostIndex)).setMaxSchedulingMemory(((HostVo)hostInfo.get(destinationHostIndex)).getMaxSchedulingMemory().add(vmMemoryInstalled.toBigInteger()));
			((HostVo)hostInfo.get(destinationHostIndex)).setCpuVmUsed(((HostVo)hostInfo.get(destinationHostIndex)).getCpuVmUsed() - cpuVmUsed);
		}
		return hostInfo;
	}

	public List<ConsolidationVo> migrationOrdering(List<ConsolidationVo> migrationSchedule, List<HostVo> hostInfo) {
		List<ConsolidationVo> ordered_migrationScheduleInfo = new ArrayList<>();
		List<String> destinationHost = new ArrayList<>();
		for (int i = 0; i < migrationSchedule.size(); i++)
			destinationHost.add(((ConsolidationVo)migrationSchedule.get(i)).getHostId());
		destinationHost = (List<String>)destinationHost.parallelStream().distinct().collect(Collectors.toList());
		List<VmVo> vmInfo = new ArrayList<>();
		if (destinationHost.size() == 1) {
			String sourceVmId = null;
			int sourceVM;
			for (sourceVM = 0; sourceVM < migrationSchedule.size(); sourceVM++) {
				if (((String)destinationHost.get(0)).equals(((ConsolidationVo)migrationSchedule.get(sourceVM)).getHostId())) {
					sourceVmId = ((ConsolidationVo)migrationSchedule.get(sourceVM)).getVmId();
					break;
				}
			}
			for (sourceVM = 0; sourceVM < hostInfo.size(); sourceVM++) {
				if (((String)destinationHost.get(0)).equals(((HostVo)hostInfo.get(sourceVM)).getId())) {
					vmInfo = ((HostVo)hostInfo.get(sourceVM)).getVms();
					break;
				}
			}
			sourceVM = migrationSchedule.size();
			int destinationVM = vmInfo.size();
			if (vmInfo.stream().allMatch(vx -> vx.getStatus().equals("up"))) {
				if (sourceVM > destinationVM) {
					for (VmVo vmVo : vmInfo) {
						ConsolidationVo migrationInfo =
								KarajanModelsKt.toConsolidationVoPostMigration(vmVo, hostInfo, sourceVmId);
						ordered_migrationScheduleInfo.add(migrationInfo);
					}
				} else {
					ordered_migrationScheduleInfo.addAll(migrationSchedule);
				}
			} else {
				ordered_migrationScheduleInfo.addAll(migrationSchedule);
			}
		} else if (destinationHost.size() > 1) {
			ordered_migrationScheduleInfo.addAll(migrationSchedule);
		}
		return ordered_migrationScheduleInfo;
	}

	public List<ConsolidationVo> reassignVirtualMachine(KarajanVo karajan, String clusterId, String shutdownHostId) {
		int clusterMemoryThreshold = karajan.getMemoryThreshold();
		List<ConsolidationVo> migrationScheduleInfo = new ArrayList<>();
		List<ClusterVo> clusterInfo = karajan.getClusters();
		List<HostVo> hostInfo = new ArrayList<>();
		List<VmVo> vmInfo = new ArrayList<>();
		List<String> vmDescription = new ArrayList<>();
		int i;
		for (i = 0; i < clusterInfo.size(); i++) {
			if (clusterId.equals(((ClusterVo)clusterInfo.get(i)).getId())) {
				hostInfo = ((ClusterVo)clusterInfo.get(i)).getHosts();
				break;
			}
		}
		for (i = 0; i < hostInfo.size(); i++) {
			if (shutdownHostId.equals(((HostVo)hostInfo.get(i)).getId())) {
				vmInfo = ((HostVo)hostInfo.get(i)).getVms();
				break;
			}
		}
		for (i = 0; i < vmInfo.size(); i++)
			vmDescription.add("");
		for (i = hostInfo.size() - 1; i >= 0; i--) {
			if (shutdownHostId.equals(((HostVo)hostInfo.get(i)).getId()) || !((HostVo)hostInfo.get(i)).getStatus().equals("up"))
				hostInfo.remove(i);
		}
		AscendingHostComparator hostComparator = new AscendingHostComparator();
		Collections.sort(hostInfo, hostComparator);
		AscendingVmComparator vmComparator = new AscendingVmComparator();
		Collections.sort(vmInfo, vmComparator);
		for (i = vmInfo.size() - 1; i >= 0; i--) {
			if (((VmVo)vmInfo.get(i)).getPlacementPolicy().equals("migratable")) {
				BigDecimal vmMemoryInstalled = ((VmVo)vmInfo.get(i)).getMemoryInstalled();
				for (int j = 0; j < hostInfo.size(); j++) {
					BigDecimal tempMemoryTotal = ((HostVo)hostInfo.get(j)).getMemoryTotal();
					BigDecimal availableMemory = tempMemoryTotal.multiply(new BigDecimal(clusterMemoryThreshold / 100.0D));
					BigDecimal hostMaxSchedulingMemory = (new BigDecimal(((HostVo)hostInfo.get(j)).getMaxSchedulingMemory())).multiply(new BigDecimal(clusterMemoryThreshold / 100.0D));
					if (availableMemory.compareTo(vmMemoryInstalled) != -1 && hostMaxSchedulingMemory.compareTo(vmMemoryInstalled) != -1) {
						int vmVCpu = ((VmVo)vmInfo.get(i)).getCores() * ((VmVo)vmInfo.get(i)).getSockets() * ((VmVo)vmInfo.get(i)).getThreads();
						int HostVCpu = ((HostVo)hostInfo.get(j)).getCores() * ((HostVo)hostInfo.get(j)).getSockets() * ((HostVo)hostInfo.get(j)).getThreads();
						if (HostVCpu > vmVCpu + ((HostVo)hostInfo.get(j)).getCpuVmUsed()) {
							((HostVo)hostInfo.get(j)).setCpuVmUsed(vmVCpu + ((HostVo)hostInfo.get(j)).getCpuVmUsed());
							((HostVo)hostInfo.get(j)).setMemoryUsed(((HostVo)hostInfo.get(j)).getMemoryUsed().add(vmMemoryInstalled));
							((HostVo)hostInfo.get(j)).setMemoryFree(((HostVo)hostInfo.get(j)).getMemoryTotal().subtract(((HostVo)hostInfo.get(j)).getMemoryUsed()));
							((HostVo)hostInfo.get(j)).setMaxSchedulingMemory(hostMaxSchedulingMemory.subtract(vmMemoryInstalled).toBigInteger());
							ConsolidationVo migrationInfo =
									KarajanModelsKt.toConsolidationVo(vmInfo.get(i), vmDescription.get(i), hostInfo.get(i));
							migrationScheduleInfo.add(migrationInfo);
							vmInfo.remove(i);
							vmDescription.remove(i);
							break;
						}
						vmDescription.set(i, "CPU 공간이 여유있는 호스트가 존재하지 않습니다.");
					} else {
						vmDescription.set(i, "메모리 공간이 여유있는 호스트가 존재하지 않습니다.");
					}
				}
			}
		}
		for (i = 0; i < vmInfo.size(); i++) {
			ConsolidationVo migrationInfo =
					KarajanModelsKt.toConsolidationVo(vmInfo.get(i), vmDescription.get(i), null);
			migrationScheduleInfo.add(migrationInfo);
		}
		return migrationScheduleInfo;
	}

	public int findHostIndex(List<HostVo> hostInfo, String hostId) {
		int index = -1;
		for (int i = 0; i < hostInfo.size(); i++) {
			if (((HostVo)hostInfo.get(i)).getId().equals(hostId)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public int findVmIndex(List<VmVo> vmInfo, String vmId) {
		int index = -1;
		for (int i = 0; i < vmInfo.size(); i++) {
			if (((VmVo)vmInfo.get(i)).getId().equals(vmId)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public static <T> List<T> clone(List<T> original) {
		List<T> copy = (List<T>)original.stream().collect(Collectors.toList());
		return copy;
	}

	static class AscendingBigDecimal implements Comparator<BigDecimal> {
		public int compare(BigDecimal a, BigDecimal b) {
			return b.compareTo(a);
		}
	}

	static class AscendingHostComparator implements Comparator<HostVo> {
		public int compare(HostVo firstHost, HostVo secondHost) {
			BigDecimal firstValue = firstHost.getMemoryFree().divide(firstHost.getMemoryTotal(), 2, 4);
			BigDecimal secondValue = secondHost.getMemoryFree().divide(secondHost.getMemoryTotal(), 2, 4);
			return firstValue.compareTo(secondValue) * -1;
		}
	}

	static class AscendingVmComparator implements Comparator<VmVo> {
		public int compare(VmVo firstVm, VmVo secondVm) {
			BigDecimal firstValue = firstVm.getMemoryUsed();
			BigDecimal secondValue = secondVm.getMemoryUsed();
			return firstValue.compareTo(secondValue) * -1;
		}
	}

	static class DescendingHostComparator implements Comparator<HostVo> {
		public int compare(HostVo firstHost, HostVo secondHost) {
			BigDecimal firstValue = firstHost.getMemoryFree().divide(firstHost.getMemoryTotal(), 2, 4);
			BigDecimal secondValue = secondHost.getMemoryFree().divide(secondHost.getMemoryTotal(), 2, 4);
			return firstValue.compareTo(secondValue);
		}
	}

	static class DescendingVmComparator implements Comparator<VmVo> {
		public int compare(VmVo firstVm, VmVo secondVm) {
			BigDecimal firstValue = firstVm.getMemoryUsed();
			BigDecimal secondValue = secondVm.getMemoryUsed();
			return firstValue.compareTo(secondValue);
		}
	}
}
