package gmc.project.infrasight.presentationservice.entities.embedded;

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

	private Double cpuPerformance;

	private Boolean isActive;

	private LocalDateTime capturedAt;
	
	public StatsEntity() {
		this.isActive = false;
		this.capturedAt = LocalDateTime.now();
	}
	
	public StatsEntity(String cpuLine, String ramLine) {
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
		
		this.availableRam = Long.parseLong(memavail[1]);
		this.totalRam = Long.parseLong(totlram[1]);
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
