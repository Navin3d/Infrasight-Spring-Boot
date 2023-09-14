package gmc.project.infrasight.statscaptureservice.services;

import gmc.project.infrasight.statscaptureservice.entities.embedded.DiscStatsEntity;

public interface StatsService {
	public void storeDiscStat(DiscStatsEntity discEntity, String host);
}
