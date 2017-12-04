/**
 * This class enumerates the possible states (the authority part is stateless, provider is stateful) of the dispute resolving protocol.
 */


package data;


	
	public enum DisputeState {
		CREATED, SENDERROR, SENTTOAUTHORITY, RESULTRECEIVED, RESULTERROR;
		
		private DisputeState() {
			
		}
	}



