import { getTimeAgo } from "./timeofcard.js";

function getReactionLabel(reactionType) {
  if (reactionType === "FEEL_THIS") return "❤️ I feel this";
  if (reactionType === "FUNNY") return "😂 That's funny";
  if (reactionType === "STAY_STRONG") return "💪 Stay strong";
  if (reactionType === "BEEN_THERE") return "✨ Been there";
  return reactionType;
}

export function createReactionSection(confession) {
  const sortedReactions = [...confession.reactions].sort((a, b) => b.count - a.count);

  return `
    <div class="reaction-section">
      ${sortedReactions.map(reaction => `
        <button 
          class="reaction-btn"
          data-id="${confession.id}"
          data-reaction="${reaction.reactionType}">
          ${getReactionLabel(reaction.reactionType)}
          <span class="reaction-count">${reaction.count}</span>
        </button>
      `).join("")}
    </div>
  `;
}

export function createCommentsSection(confession) {
  return `
    <div class="comments-wrapper">
      <div class="comments-header">
        <span class="comment-count-text">💬 ${confession.commentCount} comments</span>
        <button class="toggle-comments-btn" data-id="${confession.id}">
          Show Comments
        </button>
      </div>

      <div class="comments-list" style="display: none;">
        ${confession.comments.map(comment => `
          <div class="comment-item">
            <div class="comment-top">
              <strong>${comment.username}</strong>
              <span>${getTimeAgo(comment.createdAt)}</span>
            </div>
            <p>${comment.text}</p>
          </div>
        `).join("")}
      </div>
    </div>
  `;
}

export function createConfessionCard(confession, currentUser) {
  const isOwner = currentUser && confession.userId === currentUser.id;

  return `
    <div class="confession-card" data-id="${confession.id}" data-created-at="${confession.createdAt}" data-mood="${confession.mood}" data-category="${confession.category}">
     
<div class="card-header">
  <div class="card-header-left">
    <div class="avatar-circle">
      ${confession.name === 'Anonymous' || !confession.name 
        ? `Anon` 
        : confession.name.charAt(0).toUpperCase()}
    </div>
    <div class="header-info">
      <h3>${confession.name === 'Anonymous' || !confession.name 
        ? `Anonymous #${confession.id?.toString().slice(-4) || Math.floor(1000 + Math.random() * 9000)}` 
        : confession.name}</h3>
      <span class="time-text">${getTimeAgo(confession.createdAt)}</span>
      
    </div>
  </div>
  <p><strong>Mood:</strong> ${confession.mood}</p>
      <p><strong>Category:</strong> ${confession.category}</p>
</div>

      
      <p class="confession-text">${confession.text}</p>

      ${createReactionSection(confession)}

      <div class="comment-action-row">
        <button class="add-comment-btn" data-id="${confession.id}">💬 Add Comment</button>
      </div>

      ${createCommentsSection(confession)}

      ${
        isOwner
          ? `
            <div class="card-actions">
              <button class="delete-btn" data-id="${confession.id}">Delete</button>
              <button class="edit-btn" data-id="${confession.id}">Edit</button>
            </div>
          `
          : ""
      }
    </div>
  `;
}