package com.byteflair.parkplan.api.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.byteflair.parkplan.api.domain.type.FenceStatus;
import com.byteflair.parkplan.api.domain.type.TrackType;

@StaticMetamodel(Fence.class)
public class Fence_ {
	public static volatile SingularAttribute<Fence, Long> id;
	public static volatile SingularAttribute<Fence, String> name;
	public static volatile SingularAttribute<Fence, Date> created;
	public static volatile SingularAttribute<Fence, FenceStatus> status;
	public static volatile SingularAttribute<Fence, TrackType> trackType;
	public static volatile SingularAttribute<Fence, BigDecimal> lat;
	public static volatile SingularAttribute<Fence, BigDecimal> lng;
	public static volatile SingularAttribute<Fence, BigDecimal> radius;
	public static volatile SetAttribute<Fence, FencePairing> pairings;
}