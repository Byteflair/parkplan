package com.byteflair.parkplan.api.domain.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.byteflair.parkplan.api.domain.Pairing;
import com.byteflair.parkplan.api.domain.Pairing_;
import com.byteflair.parkplan.api.domain.User;
import com.byteflair.parkplan.api.domain.User_;
import com.byteflair.parkplan.api.domain.type.PairingStatus;

public class PairingSpecifications {
	public static Specification<Pairing> find(final Long controller, final Long controlled, final PairingStatus status) {

		return new Specification<Pairing>() {
			@Override
			public Predicate toPredicate(Root<Pairing> pairingRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (controller != null) {
					predicates.add(cb.equal(pairingRoot.<User> join(Pairing_.controller).<Long> get(User_.id), controller));
				}
				
				if (controlled != null) {
					predicates.add(cb.equal(pairingRoot.<User> join(Pairing_.controlled).<Long> get(User_.id), controlled));
				}
				
				if (status != null) {
					predicates.add(cb.equal(pairingRoot.<PairingStatus> get(Pairing_.status), status));
				}
				
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}