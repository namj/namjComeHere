package a03;

import java.util.List;

import javax.swing.SwingWorker;

public class MediaProgressChecker extends SwingWorker<Void,Void>{
	private MainPanel main;
	
	//constructor of swing worker takes in Main panel as input to invoke update on
	public MediaProgressChecker(MainPanel mp) {
		main = mp;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		//after a media is opened, continuously checks for media progress
		//when video is finish and not playable, restart
		while (true) {
			Thread.sleep(50);
			publish();
			if (main.getMedia().isPlayable() == false) {
				main.restart();
				break;
			}
		}
		return null;
	}

	@Override
	protected void process(List<Void> chunks) {
		//update progress bar UI, every publish()
		main.updateMediaProgress();
	}

}
