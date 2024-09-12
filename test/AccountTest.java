import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;


public class AccountTest {
	
	Account me, her, another;
	
	@Before
	public void setUp() throws Exception {
		me = new Account("Hakan");
		her = new Account("Serra");
		another = new Account("Cecile");
	}
	private void sendAndAcceptFriendship(Account oneAccount, Account anotherAccount) {
		oneAccount.requestFriendship(anotherAccount);
		anotherAccount.friendshipAccepted(oneAccount);
	}
	@Test
	public void sentFriendRequestToAccountsOfPeopleWhoAreNotFriendsAndAddRequestInIncomingRequestList() {
		me.requestFriendship(her);
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void ifNoFriendRequestSentNoRequestInIncomingRequest() {
		assertEquals(0, me.getIncomingRequests().size());
	}
	
	@Test
	public void testMultipleFriendRequests() {
		me.requestFriendship(her);
		me.requestFriendship(another);
		assertEquals(2, me.getIncomingRequests().size());
		assertTrue(me.getIncomingRequests().contains(another.getUserName()));
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void doubleFriendRequestsToSameAccountOnlyAddOneRequest() {
		me.requestFriendship(her);
		me.requestFriendship(her);
		assertEquals(1, me.getIncomingRequests().size());
	}
	
	@Test
	public void afterAcceptingFriendRequestWhoWantsToBeFriendsUpdated() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void threeAccountsSendEachOtherFriendRequestsAndAcceptFriendRequestsWhetherAllPeopleCanBecomeFriends() {
		sendAndAcceptFriendship(me, her);
		sendAndAcceptFriendship(me, another);
		sendAndAcceptFriendship(another, her);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(me.hasFriend(another.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
		assertTrue(her.hasFriend(another.getUserName()));
		assertTrue(another.hasFriend(her.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
	}
	
	@Test
	public void cannotBeFriendsWithAnExistingFriend() {
		sendAndAcceptFriendship(me, her);
		assertTrue(her.hasFriend(me.getUserName()));
		me.requestFriendship(her);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
		assertFalse(her.getIncomingRequests().contains(me.getUserName()));
	}

	@Test
	public void showTheOwnerAccountFriendList(){
		sendAndAcceptFriendship(me, her);
		assertTrue(her.hasFriend(me.getUserName()));
		Set<String> friendList=  me.getFriends();
		assertThat(friendList, hasItem("Serra"));
	}

	@Test
	public void afterRequestFriendOutgoingRequestsListUpdate(){
		her.requestFriendship(me);
		assertEquals(1,me.getOutgoingRequests().size());
		assertThat(me.getOutgoingRequests(),hasItem("Serra"));
	}

	@Test
	public void afterSetAutomaticallyAddingFriendRequestWillBeFriendAutomatically(){
		her.autoAcceptFriendships();
		her.requestFriendship(me);
		Set<String> meFriendList=  me.getFriends();
		Set<String>herFriendList=  her.getFriends();
		assertThat(meFriendList, hasItem("Serra"));
		assertThat(herFriendList, hasItem("Hakan"));

	}

	@Test
	public void receivingAFriendRequestFromAccountUserNameIsNullHasNoEffect(){
		Account testAccount=new Account(null);
		me.requestFriendship(testAccount);
		Set<String> incomingList=me.getIncomingRequests();
		assertEquals(0,incomingList.size());
	}

	@Test
	public void receivingAFriendRequestFromNullAccountHasNoEffect(){
		Account testAccount=null;
		me.requestFriendship(testAccount);
		Set<String> incomingList=me.getIncomingRequests();
		assertEquals(0,incomingList.size());
	}
	@Test
	public void shouldNotBeAbleToBecomeFriendsIfOneIsNotAskedFirst(){
		me.friendshipAccepted(her);
		Set<String> meFriend=me.getFriends();
		Set<String> herFriend=her.getFriends();
		assertThat(meFriend,not(hasItem(her.getUserName())));
		assertThat(herFriend,not(hasItem(me.getUserName())));
	}


	@Test
	public void ifAutoAddingIsOffShouldNotAddFriendAutomaticallyAndComingRequestListShouldNotEmpty(){
		me.autoAcceptFriendships();
		me.turnOffAutoAcceptFriendships();
		me.requestFriendship(her);
		Set<String> meFriend=me.getFriends();
		Set<String> meIncomingList=me.getIncomingRequests();
		assertEquals(0,meFriend.size());
		assertThat(meIncomingList,hasItem("Serra"));
	}
}
