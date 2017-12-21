package battleshipz.battleshipz;

import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.ChordCallback;
import de.uniba.wiai.lspi.chord.service.Key;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class ChordNotifyCallbackImpl implements NotifyCallback{
	private static Logger log = Logger.getLogger(Main.class);
	
	private ChordImpl chordImpl;

	public ChordNotifyCallbackImpl(ChordImpl chordImpl) {
		this.chordImpl = chordImpl;
		
	}
	
	public void retrieved(ID target) {
		// TODO Auto-generated method stub
		// schuss auf ID, prüfen ob getroffen oder nicht
		// danach bradcast aufrufen, das kriegen alle knoten mit hit oder nicht
		
		
		log.debug("retrieved " +  target.toBigInteger());
	}

	public void broadcast(ID source, ID target, Boolean hit) {
		// TODO Auto-generated method stub
		// hier wird informiert was im spiel passiert wer wurde angegriffen wer wurde geschossen

		log.debug("broadcast from " +  source.toBigInteger());
	}


}
