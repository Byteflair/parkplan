package com.byteflair.parkplan.api.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.byteflair.parkplan.api.domain.type.PairingStatus;

@StaticMetamodel(Pairing.class)
public class Pairing_ {
	public static volatile SingularAttribute<Pairing, Long> id;
	public static volatile SingularAttribute<Pairing, User> controller;
	public static volatile SingularAttribute<Pairing, User> controlled;
	public static volatile SingularAttribute<Pairing, PairingStatus> status;
	public static volatile SetAttribute<Pairing, FencePairing> tracks;
}