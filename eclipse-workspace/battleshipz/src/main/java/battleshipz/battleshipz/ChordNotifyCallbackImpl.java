package battleshipz.battleshipz;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.ChordCallback;
import de.uniba.wiai.lspi.chord.service.Key;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class ChordNotifyCallbackImpl implements NotifyCallback {
	private static Logger log = Logger.getLogger(Main.class);

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
			System.out.println("retrieved " + target.toHumanID());
			int shot = game.amIShoot(target);
			
			if(shot != -1) {
				chord.broadcast(target, shot == 1);
			}
			
			if (game.shipsLeft()) {
				
				final ID id2shoot = game.shootAtShip(new HashSet<Node>(chord.getFingerTable()));
				chord.retrieveAsync(id2shoot);
			}else {
				System.out.println("nirvana");
			}
		}
	}

	public void broadcast(ID source, ID target, Boolean hit) {
		synchronized (this) {
			System.out.println("broadcast from " + source.toHumanID());
			game.broadcast(source, target, hit);
		}
	}
}
