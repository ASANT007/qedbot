package com.tkis.qedbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkis.qedbot.entity.ConsistencyTracking;
import com.tkis.qedbot.repo.ConsistencyTrackingRepo;

@Service
public class ConsistencyTrackingServiceImpl implements ConsistencyTrackingService {

	@Autowired
	ConsistencyTrackingRepo consistencyTrackingRepo;
	@Override
	public void saveConsistency(ConsistencyTracking consistencyTracking) {
		
		consistencyTrackingRepo.save(consistencyTracking);
		
	}
	@Override
	public void saveConsistency(List<ConsistencyTracking> consistencyTracking) {
		
		consistencyTrackingRepo.saveAll(consistencyTracking);
	}

}
