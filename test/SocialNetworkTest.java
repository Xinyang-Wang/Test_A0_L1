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
	
//	@Test
//	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoiningAndSizeOfNetworkEqualsOne() {
//		Set<String> members = sn.listMembers();
//		assertEquals(1, members.size());
//		assertTrue(members.contains("Hakan"));
//	}
	
//	@Test
//	public void twoPeopleCanJoinSocialNetworkAndSizeOfNetworkEqualsTwo() {
//		Account her=sn.join("Cecile");
//		Set<String> members = sn.listMembers();
//		assertEquals(2, members.size());
//		assertTrue(members.contains("Hakan"));
//		assertTrue(members.contains("Cecile"));
//	}
	
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
		Account her;
		her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}

	//------------------------------net test case by us-----------------------------------------

	@Test
	public void canSwitchAccountsWithoutLoggingOut() {
		SocialNetwork sn = new SocialNetwork();
		Account h = sn.join("Hakan");
		Account f = sn.join("Fang");
		sn.login(h);
		sn.login(f);
		assertEquals("Fang", sn.getCurrentUser().getUserName());
	}

	@Test
	public void canLoginAndReturnRightAccountHandle() {
		SocialNetwork sn = new SocialNetwork();
		Account h = sn.join("Hakan");
		Account a = sn.login(h);
		assertEquals("Hakan", a.getUserName());
		assertEquals("Hakan", sn.getCurrentUser().getUserName());
	}

	@Test
	public void afterLoginSendFriendRequestIncomingRequestShouldBeCorrect()throws NoUserLoggedInException{
		sn.login(me);
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Cecile");
		assertEquals(1,her.getIncomingRequests().size());
	}

	@Test
	public void afterAcceptingFriendshipRequestTwoUsersShouldBeFriends()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.login(her);
		sn.acceptFriendshipFrom("Hakan");
		assertTrue(her.hasFriend("Hakan"));
	}
	@Test
	public void afterAcceptingAllFriendsRequestIncomingFriendRequestShouldBeEmpty()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		Account another=sn.join("Serra");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		sn.login(another);
		sn.sendFriendshipTo("Hakan");
		sn.login(me);
		sn.acceptAllFriendships();
		assertTrue(me.getIncomingRequests().isEmpty());
	}

	@Test
	public void afterRejectingFriendshipRequestTwoUsersShouldNotBeFriends()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.login(her);
		sn.rejectFriendshipFrom("Hakan");
		assertFalse(her.hasFriend("Hakan"));
		assertTrue(her.getIncomingRequests().isEmpty());
	}

	@Test
	public void afterRejectingAllFriendsRequestIncomingFriendRequestShouldBeEmpty()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		Account another=sn.join("Serra");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		sn.login(another);
		sn.sendFriendshipTo("Hakan");
		sn.login(me);
		sn.rejectAllFriendships();
		assertTrue(me.getIncomingRequests().isEmpty());
	}

	@Test
	public void afterTurningOnAutoAcceptingUserShouldAcceptFriendRequestAutomatically()throws NoUserLoggedInException{
		sn.login(me);
		sn.autoAcceptFriendships();
		Account her=sn.join("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		assertTrue(her.hasFriend("Hakan"));
	}

	@Test
	public void afterLoginAndRemoveTargetUserSocialNetworkDontHaveThisUser(){

		Account her=sn.join("Cecile");
		sn.login(her);
		sn.leave(her);
		Collection<String> peopleList=sn.listMembers();
		assertThat(peopleList,not(hasItem("Cecile")));
	}

	@Test
	public void afterLoginAndRemoveTargetUserSocialNetworkOtherPeopleDontHaveThisFriend()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.login(her);
		sn.acceptFriendshipFrom("Hakan");
		sn.login(me);
		sn.leave(me);
		assertFalse(her.hasFriend("Hakan"));

	}

	@Test
	public void afterLoginAndRemoveTargetUserSocialNetworkAllUserOutgoingListDontHaveLeaveUser()throws NoUserLoggedInException{

		Account her=sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.login(her);
		sn.leave(her);
		assertEquals(0,me.getOutgoingRequests().size());
		assertThat(me.getOutgoingRequests(), not(hasItem("Cecile")));
	}

	@Test
	public void afterLoginAndRemoveUserFromSocialNetworkAllOutgoingListShouldBeEmpty()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.leave(me);
		Set<String> herIncomingList=her.getIncomingRequests();
		assertEquals(0,herIncomingList.size());
	}


	@Test
	public void leaveAfterBecomingFriendsShouldUnFriendEachOther()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		sn.login(me);
		sn.acceptAllFriendships();
		sn.leave();
		assertThat(her.getFriends(),not(hasItem("Hakan")));
	}

	@Test
	public void leaveShouldCLearOutgoingRequest()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.leave();
		assertEquals(0,me.getOutgoingRequests().size());
	}
	@Test
	public void AfterCancelAutoFriendShipTheNewFriendRequestNeedToAcceptManuallyIncomingListShouldNotNull()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.acceptAllFriendships();
		sn.cancelAutoAcceptFriendships();
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		assertEquals(1,me.getIncomingRequests().size());
	}

	@Test
	public void hasMemberReturnsTrueIfMemberHasJoinedSN()throws NoUserLoggedInException{

		SocialNetwork sn = new SocialNetwork();
		Account f = sn.join("Fang");
		sn.login(f);
		assertTrue(sn.hasMember("Fang"));

	}
	@Test
	public void hasMemberReturnsFalseIfMemberHasJoinedSN()throws NoUserLoggedInException{
		SocialNetwork sn = new SocialNetwork();
		Account f = sn.join("Fang");
		sn.login(f);
		assertFalse(sn.hasMember("FakeFang"));
	}

	@Test
	public void AfterLoginTwoPersonUnfriendEachOtherAsFriendAfterUnfriendingTheirFriendships()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.login(her);
		sn.acceptFriendshipFrom("Hakan");
		sn.login(me);
		sn.sendFriendshipCancellationTo("Cecile");
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}


	@Test
	public void AfterBlockOneUserCurrentUserCanNotReceiveBlockedUserFriendRequest()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		assertEquals(0,her.getOutgoingRequests().size());
		assertEquals(0,me.getIncomingRequests().size());
	}

	@Test
	public void afterBlockOneUserBlockedMemberCannotSeeLoggedInUser()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.login(her);
		Set<String> memberList=sn.listMembers();
		assertThat(memberList,not(hasItem("Hakan")));
	}

	@Test
	public void AfterUnblockOneUserCurrentUserCanReceiveBlockedUserFriendRequest()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.unblock("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		assertEquals(1,her.getOutgoingRequests().size());
		assertEquals(1,me.getIncomingRequests().size());
	}

	@Test
	public void afterUnblockOneUserBlockedMemberCanSeeLoggedInUser()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.unblock("Cecile");
		sn.login(her);
		Set<String> memberList=sn.listMembers();
		assertThat(memberList,hasItem("Hakan"));
	}

	@Test
	public void afterBlockingAFriendTheyAreNotFriend()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.login(her);
		sn.acceptFriendshipFrom("Hakan");
		sn.block("Hakan");
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));

	}

	@Test
	public void afterOneUserHasSentFriendRequestThenBlockedThisUserTheIncomingListAndOutComingListShouldBeEmpty()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		assertEquals(1,me.getIncomingRequests().size());
		assertEquals(1,her.getOutgoingRequests().size());
		sn.login(me);
		sn.block("Cecile");
		assertEquals(0,me.getIncomingRequests().size());
		assertEquals(0,her.getOutgoingRequests().size());
	}


	@Test
	public void recommendFriendShouldReturn()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		Account another=sn.join("Fang");
		Account last=sn.join("rafal");

		sn.login(me);
		sn.autoAcceptFriendships();
		sn.acceptAllFriendships();
		sn.sendFriendshipTo("Fang");
		sn.sendFriendshipTo("Cecile");


		sn.login(her);
		sn.autoAcceptFriendships();
		sn.acceptAllFriendships();
		sn.sendFriendshipTo("Fang");
		sn.sendFriendshipTo("rafal");

		sn.login(another);
		sn.autoAcceptFriendships();
		sn.acceptAllFriendships();
		sn.sendFriendshipTo("rafal");

		sn.login(last);
		sn.autoAcceptFriendships();
		sn.acceptAllFriendships();


		sn.login(me);
		Set<String> recommendList=sn.recommendFriends();
		assertEquals(1,recommendList.size());
		assertThat(recommendList,hasItem("rafal"));
	}

	@Test
	public void whenNumberOfFriendsLessThanTwoShouldRecommendNull()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		sn.login(me);
		sn.acceptAllFriendships();

		Set<String> recommendList=sn.recommendFriends();
		assertNull(recommendList);

	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginSentFriendRequestShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.sendFriendshipTo("Hakan");

	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateHasMemberShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.hasMember("Hakan");

	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateBlockShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.block("Hakan");

	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateUnblockShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.unblock("Hakan");

	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateSendFriendshipCancellationToShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.sendFriendshipCancellationTo("Hakan");

	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateAcceptFriendshipFromShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.acceptFriendshipFrom("Hakan");

	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateAcceptAllFriendshipsShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.acceptAllFriendships();

	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateRejectFriendshipFromShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.rejectFriendshipFrom("Hakan");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateRejectAllFriendshipsShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.rejectAllFriendships();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void whenNoCurrentUserNotLoginOperateAutoAcceptFriendshipsShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.autoAcceptFriendships();
	}



	@Test(expected = NoUserLoggedInException.class)
	public void leaveBeforeLoggingInShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.leave();

	}

	@Test(expected = NoUserLoggedInException.class)
	public void recommendFriendBeforeLoggingInShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.recommendFriends();

	}

	@Test(expected = NoUserLoggedInException.class)
	public void cancelAutoAcceptFriendshipsBeforeLoggingInShouldThrowException()throws NoUserLoggedInException{
		Account her=sn.join("Cecile");
		sn.cancelAutoAcceptFriendships();

	}


	@Test
	public void loginUnExistingUsersShouldReturnNull(){
		Account testUser=new Account("rafal");
		assertNull(sn.login(testUser));

	}





}


