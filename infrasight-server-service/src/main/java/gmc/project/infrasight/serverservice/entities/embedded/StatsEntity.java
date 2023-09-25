package gmc.project.infrasight.serverservice.entities.embedded;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class StatsEntity implements Serializable {

	private static final long serialVersionUID = -618501582385421256L;

	@Id
	private String id;

	private Long availableRam;

	private Long totalRam;

	private Long totalSwap;

	private Long freeSwap;
	
	private Double cpuPerformance;
	
	private Double ramPerformance;

	private Double serverLoad;

	private Boolean isActive;

	private LocalDateTime capturedAt;
	
	public StatsEntity() {
		this.isActive = false;
		this.capturedAt = LocalDateTime.now();
	}
	
	public StatsEntity(String cpuLine, String ramLine, String swapLine, String loadLine) {
		/*
		 * RAM Lines
		 */
		log.error("cpuLine: {}", cpuLine);
		String[] ramlines = ramLine.split("\n");
		String totlram[] = ramlines[0].split("\\s+");
		String memavail[] = ramlines[2].split("\\s+");
		
		/*
		 * CPU Lines
		 */
		String[] strarray = cpuLine.split("\\s+");
		Double cpuPerformance = 100 - (Double.parseDouble(strarray[11]));
		DecimalFormat twoDForm = new DecimalFormat("#.##");

		/*
		 * SWAP Lines
		 */
		String[] requiredSwapLines = swapLine.split("\n")[2].split("\\s+");
		log.error("swapLine: {}", swapLine);
		log.error("Total Swap: {}", Long.valueOf(requiredSwapLines[1].trim()));
		log.error("Free Swap: {}", Long.valueOf(requiredSwapLines[3].trim()));

		/*
		 * LOAD Lines
		 */
		Double requiredLoadLines = Double.parseDouble(loadLine.split("users,\\s+")[1].split("\\s+")[4].trim());
		log.error("loadLine: {}", loadLine);
		log.error("Load: {}", requiredLoadLines);

		this.availableRam = Long.parseLong(memavail[1]);
		this.totalRam = Long.parseLong(totlram[1]);
		this.totalSwap = Long.valueOf(requiredSwapLines[1].trim());
		this.freeSwap = Long.valueOf(requiredSwapLines[3].trim());
		this.serverLoad = Double.valueOf(twoDForm.format(requiredLoadLines));
		this.cpuPerformance = Double.valueOf(twoDForm.format(cpuPerformance));
		this.isActive = true;
		this.capturedAt = LocalDateTime.now();
	}
	
	public Long usedRam() {
		return this.totalRam - this.availableRam;
	}
	
	public void setRAM(String ramLine) {
		/*
		 * Ram Lines
		 */
		String[] ramlines = ramLine.split("\n");
		String totlram[] = ramlines[0].split("\\s+");
		String memavail[] = ramlines[2].split("\\s+");
		
		this.availableRam = Long.parseLong(memavail[1]);
		this.totalRam = Long.parseLong(totlram[1]);
		this.isActive = true;
		this.capturedAt = LocalDateTime.now();
	}
	
	public void setCPU(String cpuLine) {
		/*
		 * CPU Lines
		 */
		String[] cpulines = cpuLine.split("\n");
		String[] strarray = cpulines[3].split("\\s+");
		Double cpuPerformance = 100 - (Double.parseDouble(strarray[11]));
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		
		this.cpuPerformance = Double.valueOf(twoDForm.format(cpuPerformance));
		this.isActive = true;
		this.capturedAt = LocalDateTime.now();
	}
	
	public void setIsActive(Boolean active) {
		this.isActive = active;
	}
	
}
