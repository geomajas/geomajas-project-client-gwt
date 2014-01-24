package test;

import java.io.File;
import java.io.IOException;

public class CreateFolders {

	public static void main(String[] args) {
		File root = new File("/home/jandm/git");
		fix(root);
	}

	private static void fix(File root) {
		// System.out.println("checking " + root.getPath());
		if (root.isDirectory()) {
			if (root.getPath().endsWith("main/java")) {
				File test = new File(root.getPath().replace("src/main/java", "src/test/java"));
				if (!test.exists()) {
					System.out.println("Making "+test.getPath());
					test.mkdirs();
					File keep = new File(test, ".gitkeep");
					try {
						keep.createNewFile();
						System.out.println("Made "+keep.getPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				for (File child : root.listFiles()) {
					fix(child);
				}
			}
		}

	}
}
