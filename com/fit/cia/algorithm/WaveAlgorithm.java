package com.fit.cia.algorithm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jgrapht.ext.JGraphXAdapter;

import com.fit.cia.tree.TreeGeneration;
import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.object.Node;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;

/**
 * 
 * @author DucAnh
 *
 */
public class WaveAlgorithm implements IComputeImpactSet {
	/** Danh sach cac nodes */
	private static List<Node> nodes_ = new ArrayList<>();
	/** Danh sach tap change set */
	private static List<Node> changeSet_ = new ArrayList<>();

	private static void createAndShowGui() {
		CallGraph cg = new CallGraph(nodes_, changeSet_);
		cg.createRandomChangeSet();
		cg.calculateImpactSet();
		// System.out.println(cg.changeSetToString());
		// System.out.println(cg.impactSetToString());

		JFrame frame = new JFrame("DemoGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JGraphXAdapter<String, com.fit.cia.algorithm.CallGraph.MyEdge> graphAdapter = new JGraphXAdapter<String, com.fit.cia.algorithm.CallGraph.MyEdge>(
				cg.graph);

		mxIGraphLayout layout = new mxFastOrganicLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		frame.add(new mxGraphComponent(graphAdapter));

		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Load project
		// ProjectNode projectRootNode =
		// ProjectLoader.load(HungConfig.JSF_DUKES_FOREST_PATH);

		// Xay dung su phu thuoc
		// DependencyGeneration.parse(projectRootNode);

		// Xuat cay cau truc sang JSON
		// IToString displayer = new JsonStrategy(projectRootNode);
		// Utils.writeContentToFile(displayer.getString(),
		// HungConfig.JSON_DUKES_FOREST_PATH);

		// Phan tich JSON xay dung tree
		TreeGeneration generator = new TreeGeneration(ConfigurationOfAnh.JSON_DUKES_FOREST_PATH);
		List<Node> nodes = generator.getNodes();

		// Wave (do something here)
		List<Node> changeSet = new ArrayList<>();// add nodes later
		WaveAlgorithm wave = new WaveAlgorithm(nodes, changeSet);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}

	/**
	 * 
	 * @param nodes
	 *            Danh sach tap node trong dependency graph ma gay phu
	 *            thuoc/chiu su phu thuoc
	 * @param changeSet 
	 */
	public WaveAlgorithm(List<Node> nodes, List<Node> changeSet) {
		this.nodes_ = nodes;
		this.changeSet_ = changeSet;
		compute();
	}

	@Override
	public void compute() {
		// do something else
		CallGraph cg = new CallGraph(nodes_, changeSet_);
		cg.createRandomChangeSet();
		cg.calculateImpactSet();
		System.out.println(cg.changeSetToString());
		System.out.println(cg.coreToString());
		System.out.println(cg.impactSetToString());
	}
}