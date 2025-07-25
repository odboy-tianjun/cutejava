package cn.odboy.system.service;

import cn.hutool.core.date.BetweenFormatter.Level;
import cn.hutool.core.date.DateUtil;
import cn.odboy.constant.SystemConst;
import cn.odboy.util.FileUtil;
import cn.odboy.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@Service
public class SystemMonitorService {
    private final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * 查询服务器监控信息
     *
     * @return Map<String, Object>
     */
    public Map<String, Object> queryServerMonitorInfo() {
        Map<String, Object> resultMap = new LinkedHashMap<>(8);
        try {
            SystemInfo si = new SystemInfo();
            OperatingSystem os = si.getOperatingSystem();
            HardwareAbstractionLayer hal = si.getHardware();
            // 系统信息
            resultMap.put("sys", getSystemInfo(os));
            // cpu 信息
            resultMap.put("cpu", getCpuInfo(hal.getProcessor()));
            // 内存信息
            resultMap.put("memory", getMemoryInfo(hal.getMemory()));
            // 交换区信息
            resultMap.put("swap", getSwapInfo(hal.getMemory()));
            // 磁盘
            resultMap.put("disk", getDiskInfo(os));
            resultMap.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultMap;
    }

    /**
     * 获取磁盘信息
     *
     * @return /
     */
    private Map<String, Object> getDiskInfo(OperatingSystem os) {
        Map<String, Object> diskInfo = new LinkedHashMap<>();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        String osName = System.getProperty("os.name");
        long available = 0, total = 0;
        for (OSFileStore fs : fsArray) {
            // windows 需要将所有磁盘分区累加, linux 和 mac 直接累加会出现磁盘重复的问题, 待修复
            if (osName.toLowerCase().startsWith(SystemConst.WIN)) {
                available += fs.getUsableSpace();
                total += fs.getTotalSpace();
            } else {
                available = fs.getUsableSpace();
                total = fs.getTotalSpace();
                break;
            }
        }
        long used = total - available;
        diskInfo.put("total", total > 0 ? FileUtil.getSize(total) : "?");
        diskInfo.put("available", FileUtil.getSize(available));
        diskInfo.put("used", FileUtil.getSize(used));
        if (total != 0) {
            diskInfo.put("usageRate", df.format(used / (double) total * 100));
        } else {
            diskInfo.put("usageRate", 0);
        }
        return diskInfo;
    }

    /**
     * 获取交换区信息
     *
     * @param memory /
     * @return /
     */
    private Map<String, Object> getSwapInfo(GlobalMemory memory) {
        Map<String, Object> swapInfo = new LinkedHashMap<>();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        long total = virtualMemory.getSwapTotal();
        long used = virtualMemory.getSwapUsed();
        swapInfo.put("total", FormatUtil.formatBytes(total));
        swapInfo.put("used", FormatUtil.formatBytes(used));
        swapInfo.put("available", FormatUtil.formatBytes(total - used));
        if (used == 0) {
            swapInfo.put("usageRate", 0);
        } else {
            swapInfo.put("usageRate", df.format(used / (double) total * 100));
        }
        return swapInfo;
    }

    /**
     * 获取内存信息
     *
     * @param memory /
     * @return /
     */
    private Map<String, Object> getMemoryInfo(GlobalMemory memory) {
        Map<String, Object> memoryInfo = new LinkedHashMap<>();
        memoryInfo.put("total", FormatUtil.formatBytes(memory.getTotal()));
        memoryInfo.put("available", FormatUtil.formatBytes(memory.getAvailable()));
        memoryInfo.put("used", FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()));
        memoryInfo.put("usageRate", df.format((memory.getTotal() - memory.getAvailable()) / (double) memory.getTotal() * 100));
        return memoryInfo;
    }

    /**
     * 获取Cpu相关信息
     *
     * @param processor /
     * @return /
     */
    private Map<String, Object> getCpuInfo(CentralProcessor processor) {
        Map<String, Object> cpuInfo = new LinkedHashMap<>();
        cpuInfo.put("name", processor.getProcessorIdentifier().getName());
        cpuInfo.put("package", processor.getPhysicalPackageCount() + "个物理CPU");
        cpuInfo.put("core", processor.getPhysicalProcessorCount() + "个物理核心");
        cpuInfo.put("coreNumber", processor.getPhysicalProcessorCount());
        cpuInfo.put("logic", processor.getLogicalProcessorCount() + "个逻辑CPU");
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 默认等待300毫秒...
        long time = 300;
        Util.sleep(time);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long minCpuLoadTick = 1000;
        while (Arrays.toString(prevTicks).equals(Arrays.toString(ticks)) && time < minCpuLoadTick) {
            time += 25;
            Util.sleep(25);
            ticks = processor.getSystemCpuLoadTicks();
        }
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + ioWait + irq + softIrq + steal;
        cpuInfo.put("used", df.format(100d * user / totalCpu + 100d * sys / totalCpu));
        cpuInfo.put("idle", df.format(100d * idle / totalCpu));
        return cpuInfo;
    }

    /**
     * 获取系统相关信息,系统、运行天数、系统IP
     *
     * @param os /
     * @return /
     */
    private Map<String, Object> getSystemInfo(OperatingSystem os) {
        Map<String, Object> systemInfo = new LinkedHashMap<>();
        // jvm 运行时间
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        // 计算项目运行时间
        String formatBetween = DateUtil.formatBetween(date, new Date(), Level.HOUR);
        // 系统信息
        systemInfo.put("os", os.toString());
        systemInfo.put("day", formatBetween);
        systemInfo.put("ip", IpUtil.getLocalIp());
        return systemInfo;
    }
}
