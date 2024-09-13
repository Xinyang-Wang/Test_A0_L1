import java.util.HashSet;
import java.util.Set;


public class Account  {
	
	// the unique user name of account owner
	private String userName;
	
	// list of members who are awaiting an acceptance response from this account's owner 
	private Set<String> incomingRequests = new HashSet<String>();
	
	// list of members who are friends of this account's owner
	private Set<String> friends = new HashSet<String>();

	// list of members who the account sent request to
	private Set<String> outgoingRequestsList = new HashSet<>();

	public Set<String> blackList=new HashSet<>();

	//whether add friend automatically
	private boolean autoAdding=false;

//	public boolean loginStatus=false;

	public Account(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	// return list of members who had sent a friend request to this account's owner 
	// and are still waiting for a response
	public Set<String> getIncomingRequests() {
		return new HashSet<>(incomingRequests);
	}

	// an incoming friend request to this account's owner from another member account
	public void requestFriendship(Account fromAccount) {
		if(fromAccount==null||fromAccount.getUserName()==null){
			return;
		}
		if (!friends.contains(fromAccount.getUserName())) {
			if(autoAdding){
				this.friends.add(fromAccount.getUserName());
				fromAccount.friends.add(this.getUserName());
				return;
			}
			incomingRequests.add(fromAccount.getUserName());
			fromAccount.outgoingRequestsList.add(this.getUserName());
		}
	}

	// check if account owner has a member with user name userName as a friend
	public boolean hasFriend(String userName) {
		return friends.contains(userName);
	}

	// receive an acceptance from a member to whom a friend request has been sent and from whom no response has been received
	public void friendshipAccepted(Account toAccount) {
		if(!toAccount.incomingRequests.contains(this.getUserName())){
			return;
		}
		friends.add(toAccount.getUserName());
		toAccount.friends.add(this.getUserName());
		toAccount.incomingRequests.remove(this.getUserName());
		this.outgoingRequestsList.remove(toAccount.getUserName());

	}
	// reject an acceptance from a member to whom a friend request has been sent and from whom no response has been received
	public void friendshipRejected(Account toAccount) {
		toAccount.incomingRequests.remove(this.getUserName());
		this.outgoingRequestsList.remove(toAccount.getUserName());
	}

	// delete a friend
	public void cancelFriendship(Account toAccount) {

		this.friends.remove(toAccount.getUserName());
		toAccount.friends.remove(this.getUserName());
	}
	
	public Set<String> getFriends() {
		return friends;
	}



	public Set<String> getOutgoingRequests() {return outgoingRequestsList;};

	//Automatically accept all new friend requests sent to me
	public void autoAcceptFriendships(){

			this.autoAdding=true;

	}

	public void turnOffAutoAcceptFriendships(){
		this.autoAdding=false;
	}


}
