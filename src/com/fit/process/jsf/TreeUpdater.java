package com.fit.process.jsf;

import java.util.List;

import com.fit.object.Node;
import com.fit.process.jsf.object.Dependency;

/**
 * Cap nhat cac su phu thuoc trong cay cau truc
 * 
 * @author DucAnh
 *
 */
public class TreeUpdater {
	public static void createConnection(List<Dependency> dependenciesList) {
		for (Dependency d : dependenciesList) {
			Node nBiPhuThuoc = d.getBiPhuThuoc();
			Node nGayPhuThuoc = d.getGayPhuThuoc();

			nBiPhuThuoc.getCallers().add(nGayPhuThuoc);
			nGayPhuThuoc.getCallees().add(nBiPhuThuoc);
		}
	}
}
