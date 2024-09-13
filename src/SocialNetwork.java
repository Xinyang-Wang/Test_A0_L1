import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SocialNetwork implements ISocialNetwork {
	
	private Set<Account> accounts = new HashSet<Account>();

	private Account currentUser;

	// join SN with a new user name
	@Override
	public Account join(String userName) {
		Set<String> allMember=this.listMembers();
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
	public Set<String> listMembers() {
		Set<String> members = new HashSet<String>();
		for (Account each : accounts) {
			if(this.currentUser!=null&&each.blackList.contains(this.currentUser.getUserName())){
				continue;
			}
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



	//---------------------new method for the L1-----------------------------
	@Override
	public Account login(Account me){
		Set<String> memberList=this.listMembers();
		if(memberList.contains(me.getUserName())){
			currentUser=me;
			return currentUser;
		}
		return null;
	}

	@Override
	public boolean hasMember(String userName)throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		if (accounts.contains(findAccountForUserName(userName))) {
			return true;
		}
		return false;
	}

	@Override
	public void sendFriendshipTo(String userName)throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		Account accountForUserName = findAccountForUserName(userName);
		if(accountForUserName==null){
			return;
		}
		if(accountForUserName.blackList.contains(this.currentUser.getUserName())){
			return;
		}
		accountForUserName.requestFriendship(this.currentUser);
	}

	@Override
	public void block(String userName)throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		this.currentUser.blackList.add(userName);
		this.rejectFriendshipFrom(userName);
		this.sendFriendshipCancellationTo(userName);


	}

	@Override
	public void unblock(String userName)throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		this.currentUser.blackList.remove(userName);
	}

	@Override
	public void sendFriendshipCancellationTo(String userName)throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.cancelFriendship(this.currentUser);
	}

	@Override
	public void acceptFriendshipFrom(String userName)throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipAccepted(this.currentUser);
	}

	@Override
	public void acceptAllFriendships()throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		Set<String> incomingRequest=this.currentUser.getIncomingRequests();
		for(String processingResponseName:incomingRequest){
			this.acceptFriendshipFrom(processingResponseName,this.currentUser);
		}
	}

	@Override
	public void rejectFriendshipFrom(String userName)throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipRejected(this.currentUser);
	}
	@Override
	public void rejectAllFriendships()throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		for(String processingResponseName:this.currentUser.getIncomingRequests()){
			this.rejectFriendshipFrom(processingResponseName,this.currentUser);
		}
	}

	@Override
	public void autoAcceptFriendships()throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		this.currentUser.autoAcceptFriendships();
	}

	@Override
	public void cancelAutoAcceptFriendships()throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		this.currentUser.turnOffAutoAcceptFriendships();
	}

	@Override
	public Set<String> recommendFriends()throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		Set<String> currentUserFriend=this.currentUser.getFriends();
		List<String> AllFriend=new ArrayList<>();
		Set<String> recommendFriend=new HashSet<>();
		if(currentUserFriend.size()<2){
			return null;
		}else{
			for(String each:currentUserFriend){
				Account eachFriend=this.findAccountForUserName(each);
				for(String friend:eachFriend.getFriends()){
					AllFriend.add(friend);
				}
			}
			Map<String, Long> frequencyMap = AllFriend.stream()
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

			for(Map.Entry<String, Long> entry:frequencyMap.entrySet()){
				if(entry.getValue()>=2&&!currentUserFriend.contains(entry.getKey())&&entry.getKey()!=this.currentUser.getUserName()){
					recommendFriend.add(entry.getKey());
				}
			}
			return recommendFriend;
		}


	}

	@Override
	public void leave()throws NoUserLoggedInException{
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		Set<String> outGoingRequest=this.currentUser.getOutgoingRequests();
		Set<String> friends=this.currentUser.getFriends();
		for(String usernameWaitAccept:outGoingRequest){
			Account waitAccept=this.ExistUserAccount(usernameWaitAccept);
			this.currentUser.friendshipRejected(waitAccept);
		}

		this.rejectAllFriendshipsTo(this.currentUser);

		for(String friend:friends){
			Account waitRemoveFriend=this.findAccountForUserName(friend);
			waitRemoveFriend.cancelFriendship(this.currentUser);
		}

		this.accounts.remove(this.currentUser);
		this.currentUser=null;
	}



	public Account getCurrentUser(){

		return this.currentUser;
	}




}
