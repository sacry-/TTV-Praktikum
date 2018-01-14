package battleshipz.battleshipz;

import java.util.HashSet;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class ChordNotifyCallbackImpl implements NotifyCallback {

	private ChordImpl chord;

	private Game game;

	public ChordNotifyCallbackImpl(ChordImpl chordImpl) {
		this.chord = chordImpl;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void retrieved(ID target) {
		// shot at us
		synchronized (this) {
			int shot = game.amIShoot(target);
			
			if(shot != -1) {
				chord.broadcast(target, shot == 1);
			}
			
			if (game.shipsLeft()) {
				final ID id2shoot = game.shootAtShip(new HashSet<Node>(chord.getFingerTable()));
				Utils.out("source " + chord.getID().toHumanID() + " shooting " + target.toHumanID());
				chord.retrieveAsync(id2shoot);
			} else {
				Utils.out("I am in nirvana ciao..");
			}
		}
	}

	public void broadcast(ID source, ID target, Boolean hit) {
		synchronized (this) {
			game.broadcast(source, target, hit);
		}
	}
}
