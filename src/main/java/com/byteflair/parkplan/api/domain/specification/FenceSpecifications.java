package com.byteflair.parkplan.api.domain.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.byteflair.parkplan.api.domain.Fence;
import com.byteflair.parkplan.api.domain.FencePairing;
import com.byteflair.parkplan.api.domain.FencePairing_;
import com.byteflair.parkplan.api.domain.Fence_;
import com.byteflair.parkplan.api.domain.Pairing;
import com.byteflair.parkplan.api.domain.Pairing_;
import com.byteflair.parkplan.api.domain.User;
import com.byteflair.parkplan.api.domain.User_;
import com.byteflair.parkplan.api.domain.type.FenceStatus;

public class FenceSpecifications {
	public static Specification<Fence> find(final Long user, final FenceStatus status) {

		return new Specification<Fence>() {
			@Override
			public Predicate toPredicate(Root<Fence> tarckRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (user != null) {
					Predicate isController = cb.equal(tarckRoot.<FencePairing>join(Fence_.pairings).<Pairing>join(FencePairing_.pairing).<User>join(Pairing_.controller).<Long>get(User_.id), user);
					Predicate isControlled = cb.equal(tarckRoot.<FencePairing>join(Fence_.pairings).<Pairing>join(FencePairing_.pairing).<User>join(Pairing_.controlled).<Long>get(User_.id), user);
					
					predicates.add(cb.or(isController, isControlled));
				}
				
				if (status != null) {
					predicates.add(cb.equal(tarckRoot.<FenceStatus> get(Fence_.status), status));
				}
				
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	public static Specification<Fence> findByTopoosTrackId(final Long topoosTrackId) {

		return new Specification<Fence>() {
			@Override
			public Predicate toPredicate(Root<Fence> tarckRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(tarckRoot.<FencePairing>join(Fence_.pairings).<Long>get(FencePairing_.topoosTrackId), topoosTrackId);
			}
		};
	}
	
	public static Specification<Fence> findByPairingId(final Long pairingId) {

		return new Specification<Fence>() {
			@Override
			public Predicate toPredicate(Root<Fence> tarckRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(tarckRoot.<FencePairing>join(Fence_.pairings).<Pairing>join(FencePairing_.pairing).<Long>get(Pairing_.id), pairingId);
			}
		};
	}
}