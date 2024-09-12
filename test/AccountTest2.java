import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class AccountTest2 {
	
	Account me, her, another;
    
    // these are some example tests: you can merge them with your own tests from A0 
	
	@Before
	public void setUp() throws Exception {
		me = new Account("Hakan");
		her = new Account("Serra");
		another = new Account("Cecile");
	}

	@Test
	public void canBeFriendsWithAnother() {
		me.requestFriendship(her);
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void noFriendRequests() {
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
	public void doubleFriendRequestsAreOk() {
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
	public void everybodyAreFriends() {
		me.requestFriendship(her);
		me.requestFriendship(another);
		her.requestFriendship(another);
		her.friendshipAccepted(me);
		another.friendshipAccepted(her);
		another.friendshipAccepted(me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(me.hasFriend(another.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
		assertTrue(her.hasFriend(another.getUserName()));
		assertTrue(another.hasFriend(her.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
	}
	
	@Test
	public void cannotInitiateFriendshipWithAnExistingFriend() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertTrue(her.hasFriend(me.getUserName()));
		me.requestFriendship(her);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
		assertFalse(her.getIncomingRequests().contains(me.getUserName()));
	}

}
