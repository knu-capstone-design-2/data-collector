package com.example.monitoring;
import com.example.monitoring.ResourceMonitor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResourceMonitorTest {
    public static void main(String[] args) {
        ResourceMonitor monitor = new ResourceMonitor();
        List<String> containerIds = getRunningContainers();

        for (String containerId : containerIds) {
            Map<String, String> resources = monitor.getContainerResources(containerId);
            System.out.println("Container ID: " + containerId);
            System.out.println("CPU Usage: " + resources.get("CPU"));
            System.out.println("Memory Usage / Limit: " + resources.get("Memory Usage / Limit"));
            System.out.println("Memory Usage Percentage: " + resources.get("Memory Usage Percentage"));
            System.out.println("Net I/O: " + resources.get("Net I/O"));
            System.out.println("Block I/O: " + resources.get("Block I/O"));
            System.out.println("--------------------------");
        }
    }


    public static List<String> getRunningContainers() {
        // 실행 중인 컨테이너 ID 리스트 반환
        List<String> containerIds = new ArrayList<>();//컨테이너 id 저장할 리스트
        try {
            //docker 명령어 실행을 위한 프로세스 생성, 실행 중인 컨테이너 ID
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "ps", "--format", "{{.ID}}");
            Process process = processBuilder.start();//프로세스 실행
            //프로세스 출력 읽기 위한 BufferdReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            //실행 결과 한 줄씩 읽어서 리스트에 저장
            while ((line = reader.readLine()) != null) {
                containerIds.add(line);
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("실행 중인 컨테이너 들고 오는거 실패 : " + e.getMessage());
        }
        return containerIds;//실행 중인 컨테이너 ID 목록 return
    }
}

/**
 package com.example.monitoring;
 import com.example.monitoring.service.ResourceMonitor;
 import org.junit.jupiter.api.Test;
 import static org.junit.jupiter.api.Assertions.*;

 import java.util.Map;

 class ResourceMonitorTest {

@Test
void testGetContainerResources() {
// 테스트할 컨테이너 ID (실제로 실행 중인 컨테이너 ID로 바꿔야 함!)
String containerId = "913c988a365b";

// ResourceMonitor 인스턴스 생성
ResourceMonitor resourceMonitor = new ResourceMonitor();

// 컨테이너 리소스 사용량 가져오기
Map<String, String> resources = resourceMonitor.getContainerResources(containerId);

// 결과 검증
assertNotNull(resources, "결과가 null이 아님");
assertTrue(resources.containsKey("CPU"), "CPU 사용량이 포함되어 있어야 함");
assertTrue(resources.containsKey("Memory"), "Memory 사용량이 포함되어 있어야 함");

// 결과 출력 (테스트 실행 시 확인)
System.out.println("CPU 사용량: " + resources.get("CPU"));
System.out.println("메모리 사용량: " + resources.get("Memory"));
}
}
 */