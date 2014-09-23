package a03;

import java.io.File;

import javax.swing.SwingWorker;

public class AudioProcessor extends SwingWorker<Void,Void>{
	private enum fileTypes {
		mpg, avi, mp4, rv, divx, wmv, mov
	}
	
	private String process;
	private File mediaFile;
	private String fileType;
	
	public AudioProcessor(String process, File file) {
		this.process = process;
		this.mediaFile = file;
		for (fileTypes fT : fileTypes.values()) {
			if (file.getName().contains("." + fT)) {
				fileType = "." + fT;
			} else {
				fileType = ".mp4";
			}
		}
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		String cmd;
		if (process.equals("ex") || process.equals("rm&ex")) {
			
		}
		if (process.equals("rm") || process.equals("rm&ex")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -c:v copy -an output" + fileType;
			ProcessBuilder removerBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			Process remover = removerBuilder.start();
			remover.waitFor();
		}
		if (process.equals("ov")) {
			
		}
		return null;
	}
	
}
