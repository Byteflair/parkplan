package com.byteflair.parkplan.api.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(FencePairing.class)
public class FencePairing_ {
	public static volatile SingularAttribute<FencePairing, Fence> track;
	public static volatile SingularAttribute<FencePairing, Pairing> pairing;
	public static volatile SingularAttribute<FencePairing, Long> topoosTrackId;
}