package com.example.monitoring;
import com.example.monitoring.ComputerResourceMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComputerResourceMonitorTest {
    private ComputerResourceMonitor monitor;

    @BeforeEach
    void setUp() {
        monitor = new ComputerResourceMonitor();
    }

    //3초마다 10번 가져오기
    @Test
    void testGetComputerResources() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            double cpuUsage = monitor.getComputerCpuUsage();
            double freeMemory = monitor.getComputerFreeMemory();
            double totalMemory = monitor.getComputerTotalMemory();
            double memoryUsage = monitor.getComputerMemoryUsage();
            double diskUsage=monitor.getComputerDiskUsage();
            double totalDisk=monitor.getComputerTotalDisk();
            double freeDisk=monitor.getComputerFreeDisk();
            String IPAddress=monitor.getComputerIPAddress();
            String networkUsage=monitor.getNetworkUsage();
            String networkName=monitor.getNetworkName();

            System.out.println("==============================");
            System.out.println("CPU Usage : " + String.format("%.2f", cpuUsage)+"%");
            System.out.println("Memory Free Space : " + String.format("%.2f", freeMemory)+"GB");
            System.out.println("Memory Total Space : " + String.format("%.2f", totalMemory)+"GB");
            System.out.println("Memory Usage Space : " + String.format("%.2f", memoryUsage)+"GB");
            System.out.println("디스크 사용 용량 : " + diskUsage + " GB");
            System.out.println("전체 디스크 용량 : " + totalDisk+ " GB");
            System.out.println("디스크 남은 용량 : " + freeDisk+ " GB");
            System.out.println("localhost IP 주소 : "+IPAddress);
            System.out.println(networkUsage);
            //System.out.println("Network Name : " + networkName);




            //System.out.println("CPU Usage: " + cpuUsage + "%");
            //System.out.println("Free Memory: " + freeMemory + " GB");
            //System.out.println("Total Memory: " + totalMemory + " GB");
            //System.out.println("Memory Usage: " + memoryUsage + " GB");
            System.out.println("==============================");

            assertTrue(cpuUsage >= 0 && cpuUsage <= 100, "CPU usage should be between 0 and 100");
            assertTrue(freeMemory >= 0, "Free memory should not be negative");
            assertTrue(totalMemory > 0, "Total memory should be greater than 0");
            assertTrue(memoryUsage >= 0, "Memory usage should not be negative");

            Thread.sleep(3000);
        }
    }
}

