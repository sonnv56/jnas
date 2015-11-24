package com.fit.cia.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.fit.cia.tree.TreeGeneration;
import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.DependencyGeneration;
import com.fit.process.tostring.IToString;
import com.fit.process.tostring.JsonStrategyForHung;
import com.fit.util.Utils;

/**
 * 
 * @author DucAnh
 *
 */
public class WaveAlgorithm implements IComputeImpactSet {
	/** Danh sach cac nodes */
	private List<Node> nodes_ = new ArrayList<>();
	/** Danh sach tap change set */
	private List<Node> changeSet_ = new ArrayList<>();

	public static void main(String[] args) {
		// Load project
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);

		// Xay dung su phu thuoc
		DependencyGeneration.parse(projectRootNode);

		// Xuat cay cau truc sang JSON
		IToString displayer = new JsonStrategyForHung(projectRootNode);
		Utils.writeContentToFile(displayer.getString(), ConfigurationOfAnh.JSON_DUKES_FOREST_PATH);

		// Phan tich JSON xay dung tree
		TreeGeneration generator = new TreeGeneration(ConfigurationOfAnh.JSON_DUKES_FOREST_PATH);
		List<Node> nodes = generator.getNodes();

		// Wave (do something here)
		List<Node> changeSet = new ArrayList<>();// add nodes later
		WaveAlgorithm wave = new WaveAlgorithm(nodes, changeSet);
	}

	public WaveAlgorithm(List<Node> nodes, List<Node> changeSet) {
		this.nodes_ = nodes;
		this.changeSet_ = changeSet;
		compute();
	}

	@Override
	public void compute() {
		// do something else
	}
}