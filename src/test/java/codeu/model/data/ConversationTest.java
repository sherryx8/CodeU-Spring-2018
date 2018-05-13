// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

public class ConversationTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID owner = UUID.randomUUID();
    String title = "Test_Title";
    Instant creation = Instant.now();
    ArrayList<String> participants = new ArrayList<String>();
    participants.add("Test_Participant_1");
    participants.add("Test_Participant_2");

    Conversation conversation = new Conversation(id, owner, title, creation, participants);

    Assert.assertEquals(id, conversation.getId());
    Assert.assertEquals(owner, conversation.getOwnerId());
    Assert.assertEquals(title, conversation.getTitle());
    Assert.assertEquals(creation, conversation.getCreationTime());
    Assert.assertEquals(participants, conversation.getParticipants());

    /** addParticipant test */
    conversation.addParticipant("Test_Participant_3");
    participants.add("Test_Participant_3");

    Assert.assertEquals(participants, conversation.getParticipants());

    /** changePrivacy test */
    conversation.changePrivacy(true);  //makes private
    Assert.assertEquals(true, conversation.getPrivacyStatus());

  }
}
