package com.tkis.qedbot.service;

import java.util.List;

import com.tkis.qedbot.entity.ConsistencyTracking;

public interface ConsistencyTrackingService {

	void saveConsistency(ConsistencyTracking consistencyTracking);

	void saveConsistency(List<ConsistencyTracking> consistencyTracking);

}
