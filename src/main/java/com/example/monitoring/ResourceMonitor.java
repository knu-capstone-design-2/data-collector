//getContainerResources -> docker stats 명령어를 실행해 CPU와 메모리 사용량을 가져와 Map에 저장하고 반환
//getContainerCpuUsage -> getContainerResources를 호출하여 CPU 사용량을 가져온 후, %를 제거하고 더블로 변환하여 반환
package com.example.monitoring;

import java.io.BufferedReader; //프로세스 실행 결과 읽을때
import java.io.InputStreamReader;

import java.util.HashMap;//컨테이너 리소스 사용량 저장할 때 사용할거임.
import java.util.Map;


//특정 컨테이너의 리소스를 모니터링하는 클래스
public class ResourceMonitor {
    // 특정 컨테이너의 리소스 사용량을 가져와서 Map<String,String> 형태로 반환
    // 키는 cpu,메모리이고 값은 사용량 문자열
    public Map<String, String> getContainerResources(String containerId) {
        Map<String, String> resourceUsage = new HashMap<>(); //리소스 사용량을 저장할 공간
        try {
            //docker stats : 컨테이너 리소스 사용량을 가져오는 명령어
            //--no-stream : 실시간 스트리밍을 하지 않고 한 번만 데이터를 가져오도록 설정
            //cpu 사용량과 메모리 사용량을 출력
            //ProcessBuilder를 사용하여 명령어를 실행하는 프로세스를 생성하고 실행
            //docker stats --no-stream : 반복 출력하지 않고 현 시점의 결과 1회만 출력!!
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "stats", containerId, "--no-stream", "--format", "{{.CPUPerc}} {{.MemUsage}} {{.MemPerc}} {{.NetIO}} {{.BlockIO}}");
            Process process = processBuilder.start();

            //BufferReader : 실행된 프로세스의 출력을 읽음
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();//readLine()을 호출하여 결과를 가져옴(한 줄)
            reader.close(); //사용이 끝난 후 스트림을 닫음.

            if (line != null && !line.isEmpty()) {//line이 null이 아니고 비어있지 않다면, " "을 기준으로 분리함
                String[] usageData = line.split(" ");
                //String[] usageData = line.trim().split("\\s+");
                if (usageData.length >= 5) {
                    resourceUsage.put("CPU", usageData[0]); // 0 번째 값은 CPU 사용량이니 "CPU"키로 저장
                    /**
                     resourceUsage.put("Memory Usage / Limit", usageData[1]); // 두 번째와 세 번째 값이 메모리 사용량이니까 "Memory"키로 저장
                     resourceUsage.put("Memory Usage Percentage", usageData[2] );
                     resourceUsage.put("Net I/O", usageData[3] );
                     resourceUsage.put("Block I/O",usageData[4]);
                     */
                    resourceUsage.put("Memory Usage / Limit", usageData[1] + " / " + usageData[3]); // 첫 번째와 세 번째 값이 메모리 사용량이니까 "Memory"키로 저장
                    resourceUsage.put("Memory Usage Percentage", usageData[4] );//4번째에 메모리 사용률? 있음
                    resourceUsage.put("Net I/O", usageData[5] + " / " + usageData[7]);//NET I/O
                    resourceUsage.put("Block I/O",usageData[8] + " / " + usageData[10]);//BLOCK I/O

                }
            }
        } catch (Exception e) {//예외 발생 시 오류 메시지 출력
            System.err.println("Failed to get container resources: " + e.getMessage());
        }
        return resourceUsage; //CPU,메모리 사용량이 저장된 Map<String,String>을 반환
    }

    /**
     * 특정 컨테이너의 CPU 사용량을 double 값으로 반환하는 메서드
     * @param containerId 모니터링할 컨테이너의 ID
     * @return CPU 사용량 (예: 5.32)
     */
    public double getContainerCpuUsage(String containerId) {
        Map<String, String> resources = getContainerResources(containerId);//메서드를 호출해서 CPU 및 메모리 정보를 가져옴.
        if (resources.containsKey("CPU")) { //CPU키가 존재하나 안하나
            String cpuString = resources.get("CPU").replace("%", ""); // CPU 값에서 % 기호를 제거해서 숫자로 변환할 준비를 함
            try {
                return Double.parseDouble(cpuString); // 문자열을 double로 변환
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse CPU usage: " + cpuString);//변환 중 오류가 발생하면 예외 메시지 출력
            }
        }
        return -1.0; // CPU 사용량을 가져오지 못했을 경우 -1.0을 반환해서 오류 상태 나타냄.
    }



}