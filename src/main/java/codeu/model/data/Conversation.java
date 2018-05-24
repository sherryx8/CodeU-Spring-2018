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
import java.util.ArrayList;

/**
 * Class representing a conversation, which can be thought of as a chat room. Conversations are
 * created by a User and contain Messages.
 */
public class Conversation {
  public final UUID id;
  public final UUID owner;
  public final Instant creation;
  public final String title;
  /** Store the names of participants in a particular Conversation */
  private ArrayList<String> participants;
  /** Privacy status of Conversation: 0 for Public, 1 for Private; "Public" by default */
  private String privacyStatus = "Public";

  /**
   * Constructs a new Conversation.
   *
   * @param id the ID of this Conversation
   * @param owner the ID of the User who created this Conversation
   * @param title the title of this Conversation
   * @param creation the creation time of this Conversation
   */
  public Conversation(UUID id, UUID owner, String title, Instant creation, ArrayList<String> participants) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.participants = participants;
  }

  /** Returns the ID of this Conversation. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the User who created this Conversation. */
  public UUID getOwnerId() {
    return owner;
  }

  /** Returns the title of this Conversation. */
  public String getTitle() {
    return title;
  }

  /** Returns the creation time of this Conversation. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the privacy status of this Conversation. */
  public String getPrivacyStatus(){
    return privacyStatus;
  }

  /** Changes privacy status of this Conversation to given status. */
  public void setPrivacyStatus(String status){
    this.privacyStatus = status;
  }

  /** Returns the names of participants in this Conversation. */
  public ArrayList<String> getParticipants() {
    return participants;
  }

  /** Adds a member to the participants. */
  public void addParticipant(String userName) {
    participants.add(userName);
  }


  /** Checks if userName is participant of this conversation. */
  public boolean checkIfParticipant(String userName){
      return participants.contains(userName);
  }

  /** Deletes a member of a participant. */
  public void deleteParticipant(String userName) {
    participants.remove(userName);

  }
}
