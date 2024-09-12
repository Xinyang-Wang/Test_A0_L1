import java.util.HashSet;
import java.util.Collection;
import java.util.Set;

public class SocialNetwork {
	
	private Collection<Account> accounts = new HashSet<Account>();

	// join SN with a new user name
	public Account join(String userName) {
		Collection<String> allMember=this.listMembers();
		if(allMember.contains(userName) || userName==null||userName==""){
			return null;
		}
		Account newAccount = new Account(userName);
		accounts.add(newAccount);
		return newAccount;
	}

	// find a member by user name 
	private Account findAccountForUserName(String userName) {
		// find account with user name userName
		// not accessible to outside because that would give a user full access to another member's account
		for (Account each : accounts) {
			if (each.getUserName().equals(userName))
					return each;
		}
		return null;
	}
	
	// list user names of all members
	public Collection<String> listMembers() {
		Collection<String> members = new HashSet<String>();
		for (Account each : accounts) {
			members.add(each.getUserName());
		}
		return members;
	}
	
	// from my account, send a friend request to user with userName from my account
	public void sendFriendshipTo(String userName, Account me) {

		Account accountForUserName = findAccountForUserName(userName);
		if(accountForUserName==null){
			return;
		}
		accountForUserName.requestFriendship(me);
	}

	// from my account, accept a pending friend request from another user with userName
	public void acceptFriendshipFrom(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipAccepted(me);
	}

	// from my account, reject a pending friend request from another user with userName
	public void rejectFriendshipFrom(String herUserName, Account me) {
		Account accountForUserName = findAccountForUserName(herUserName);
		accountForUserName.friendshipRejected(me);
	}

	public void sendFriendshipCancellationTo(String herUserName, Account me) {
		Account accountForUserName = findAccountForUserName(herUserName);
		accountForUserName.cancelFriendship(me);
	}

	// Accept all friend requests that are pending a response from account
	public void acceptAllFriendshipsTo(Account me){
		Set<String> incomingRequest=me.getIncomingRequests();
		for(String processingResponseName:incomingRequest){
			this.acceptFriendshipFrom(processingResponseName,me);
		}
	}
	// Reject all friend requests that are pending a response from account
	public void rejectAllFriendshipsTo(Account me){
		for(String processingResponseName:me.getIncomingRequests()){
			this.rejectFriendshipFrom(processingResponseName,me);
		}


	}

	//set automatically adding friend in the network
	public void autoAcceptFriendshipsTo(Account me){
		me.autoAcceptFriendships();
	}

	//user leave, remove all user information
	public void leave(Account me){
//		Set<String> incomingRequest=me.getIncomingRequests();
		Set<String> outGoingRequest=me.getOutgoingRequests();
		Set<String> friends=me.getFriends();
		for(String usernameWaitAccept:outGoingRequest){
			Account waitAccept=this.ExistUserAccount(usernameWaitAccept);
			me.friendshipRejected(waitAccept);
		}

		this.rejectAllFriendshipsTo(me);

		for(String friend:friends){
			Account waitRemoveFriend=this.findAccountForUserName(friend);
			waitRemoveFriend.cancelFriendship(me);
		}

		this.accounts.remove(me);
		me=null;
	}

	//to test input user name, return Account
	public Account ExistUserAccount(String username){
		Account testAccount=this.findAccountForUserName(username);
		if(testAccount==null){
			return null;
		}else{
			return testAccount;
		}
	}
}
