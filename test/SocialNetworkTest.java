import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;


public class SocialNetworkTest {

	private SocialNetwork sn;
	private Account me;
	@Before
	public void setUp() throws Exception {
		sn=new SocialNetwork();
		me=sn.join("Hakan");
	}

	@After
	public void tearDown() throws Exception {
	
	}


	@Test
	public void UserIsAddedToTheNetwork() {
		Account her=sn.join("Cecile");
		assertNotNull(her);
		assertEquals("Cecile", her.getUserName());
	}
	
	@Test 
	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoiningAndSizeOfNetworkEqualsOne() {
		Collection<String> members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}
	
	@Test 
	public void twoPeopleCanJoinSocialNetworkAndSizeOfNetworkEqualsTwo() {
		Account her=sn.join("Cecile");
		Collection<String> members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void friendRequestIsSentAndIncomingRequestsContainThisRequest() {
		// test sending friend request
		Account her=sn.join("Cecile");
		assertNotNull(me);
		assertNotNull(her);
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(her.getIncomingRequests().contains("Hakan"));

	}
	
	@Test 
	public void twoPersonAddEachOtherAsFriendAfterAcceptingTheFriendRequest() {

		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}
	@Test
	public void twoPersonUnfriendEachOtherAsFriendAfterUnfriendingTheirFriendships() {

		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		sn.sendFriendshipCancellationTo("Cecile",me);
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}
	@Test
	public void incomingRequestIsNullAfterAcceptingAllFriendRequests(){
		Account her=sn.join("Cecile");
		Account another=sn.join("Serra");
		sn.sendFriendshipTo("Hakan",her);
		sn.sendFriendshipTo("Hakan",another);
		sn.acceptAllFriendshipsTo(me);
		assertTrue(me.getIncomingRequests().isEmpty());
	}

	@Test
	public void canRejectAFriendRequest() {
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.rejectFriendshipFrom("Hakan", her);
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}

	@Test
	public void IncomingRequestDoesNotContainRejectedRequest(){
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.rejectFriendshipFrom("Hakan", her);
		assertFalse(her.getIncomingRequests().contains("Hakan"));
	}
	@Test
	public void OutgoingRequestDoesNotContainRejectedRequest(){
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.rejectFriendshipFrom("Hakan", her);
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test
	public void IncomingRequestIsNullAfterRejectingAllFriendRequests(){
		Account her=sn.join("Cecile");
		Account another=sn.join("Serra");
		sn.sendFriendshipTo("Hakan",her);
		sn.sendFriendshipTo("Hakan",another);
		sn.rejectAllFriendshipsTo(me);
		assertTrue(me.getIncomingRequests().isEmpty());
	}



	@Test
	public void AfterReceiveRequestFriendPeopleWhoSetAutomaticallyAddingInSocialNetworkCanBeFriendAutomatically(){
		sn.autoAcceptFriendshipsTo(me);
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Hakan",her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));

	}


	@Test
	public void afterRemoveTargetUserSocialNetworkDontHaveThisUser(){
		Account her=sn.join("Cecile");
		sn.leave(her);
		Collection<String> peopleList=sn.listMembers();
		assertThat(peopleList,not(hasItem("Cecile")));
	}

	@Test
	public void afterRemoveTargetUserSocialNetworkOtherPeopleDontHaveThisFriend(){
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile",me);
		sn.acceptFriendshipFrom("Hakan", her);
		sn.leave(me);
		assertFalse(her.hasFriend("Hakan"));

	}

	@Test
	public void afterRemoveTargetUserSocialNetworkAllUserOutgoingListDontHaveLeaveUser(){
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile",me);
		sn.leave(her);
		assertEquals(0,me.getOutgoingRequests().size());
		assertThat(me.getOutgoingRequests(), not(hasItem("Cecile")));
	}

	@Test
	public void afterRemoveUserFromSocialNetworkAllOutgoingListShouldBeEmpty(){
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile",me);
		sn.leave(me);
		Set<String> herIncomingList=her.getIncomingRequests();
		assertEquals(0,herIncomingList.size());
	}

	@Test
	public void sendingAFriendRequestToNonExistingAccountShouldHaveNoEffect(){
		sn.sendFriendshipTo("Cecile",me);
		Set<String> outgoingList=me.getOutgoingRequests();
		assertThat(outgoingList,not(hasItem("Cecile")));

	}

	@Test
	public void tryingToJoinSocialNetworkIfAlreadyAMemberShouldNotWork(){
		Account anotherMe=sn.join("Hakan");
		assertNull(anotherMe);
	}

	@Test
	public void tryingToJoinTheSocialNetworkWithNullUserNameShouldReturnNullAccount(){
		Account testAccount=sn.join(null);
		assertNull(testAccount);
		Collection<String> memberList=sn.listMembers();
		assertEquals(1,memberList.size());
	}

	@Test
	public void tryingToJoinTheSocialNetworkWithEmptyUserNameShouldReturnNullAccount(){
		Account testAccount=sn.join("");
		assertNull(testAccount);
		Collection<String> memberList=sn.listMembers();
		assertEquals(1,memberList.size());
	}

	@Test
	public void ifUsernameNotInSocialNetworkShouldReturnNullAccount(){
		Account testResult=sn.ExistUserAccount("Cecile");
		assertNull(testResult);
	}

	@Test
	public void ifUsernameInSocialNetworkShouldReturnAccount(){
		Account testResult=sn.ExistUserAccount("Hakan");
		assertNotNull(testResult);
	}



//---------------------new test--------------------------------
@Test
public void canJoinSocialNetwork() {
	SocialNetwork sn = new SocialNetwork();
	Account me = sn.join("Hakan");
	assertEquals("Hakan", me.getUserName());
}

	@Test
	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoiningAndSizeOfNetworkEqualsOne() {
		SocialNetwork sn = new SocialNetwork();
		sn.join("Hakan");
		Set<String> members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}

	@Test
	public void twoPeopleCanJoinSocialNetworkAndSizeOfNetworkEqualsTwo() {
		SocialNetwork sn = new SocialNetwork();
		sn.join("Hakan");
		sn.join("Cecile");
		Set<String> members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}

	@Test
	public void sendAndAcceptFriendRequestToBecomeFriends() {
		// test sending friend request
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}
}


