package com.fit.process.jsf;

import com.fit.object.Node;

/**
 * Dai dien mot dependency tu component A toi component B
 * 
 * @author DucAnh
 *
 */
public class Dependency {
	private Node biPhuThuoc;
	private Node gayPhuThuoc;

	public Node getBiPhuThuoc() {
		return biPhuThuoc;
	}

	public void setBiPhuThuoc(Node biPhuThuoc) {
		this.biPhuThuoc = biPhuThuoc;
	}

	public Node getGayPhuThuoc() {
		return gayPhuThuoc;
	}

	public void setGayPhuThuoc(Node gayPhuThuoc) {
		this.gayPhuThuoc = gayPhuThuoc;
	}
}
