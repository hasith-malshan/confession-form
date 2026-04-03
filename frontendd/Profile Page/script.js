// ===================== STATE =====================
var state = {
  username: "Anonymous #2841",
  memberSince: "Jan 2025",   // set at account creation, never editable
  status: "Online",          // auto-managed by page visibility
  streak: 5,
  moodData: { Happy: 5, Sad: 9, Angry: 3, Love: 4, Fear: 3 },
  categories: { Relationships: 8, School: 6, Work: 5, Embarrassing: 3, "Late Night": 2 },
  confessions: [
    {
      id: 1,
      mood: "Sad", cat: "Relationships", time: "2 hours ago",
      text: '"I still check my ex\'s Instagram every morning even though it hurts me every time. I know I should stop but I just can\'t help myself..."',
      reactions: { "❤️": 12, "😢": 5, "🤗": 3 },
      comments: [
        { id: 1, author: "Anonymous #4421", time: "1 hour ago",
          text: "I feel the same way. It's one of the hardest habits to break. You're not alone in this 💜",
          likes: 7, liked: false, reactions: { "❤️": 3 }, userReaction: null },
        { id: 2, author: "Anonymous #8832", time: "45 min ago",
          text: "It gets better with time, I promise. Block if you have to — your peace matters more.",
          likes: 4, liked: false, reactions: {}, userReaction: null }
      ]
    },
    {
      id: 2,
      mood: "Happy", cat: "Work", time: "Yesterday",
      text: '"Finally got the promotion I\'ve been working towards for two years! I can\'t tell my coworkers yet but I\'m bursting with joy."',
      reactions: { "❤️": 46, "🔥": 22 },
      comments: [
        { id: 1, author: "Anonymous #3310", time: "Yesterday",
          text: "Congratulations! You absolutely deserve it 🎉🔥",
          likes: 12, liked: false, reactions: { "🔥": 5, "❤️": 2 }, userReaction: null }
      ]
    }
  ],
  notifications: [
    { icon: "💬", text: "Anonymous #4421 replied to your Relationships confession", time: "1 hour ago", unread: true, confessionId: 1 },
    { icon: "💬", text: "Anonymous #8832 also replied to your Relationships confession", time: "45 min ago", unread: true, confessionId: 1 },
    { icon: "❤️", text: "Someone reacted ❤️ to your Work confession", time: "Yesterday", unread: true, confessionId: 2 },
    { icon: "🔥", text: "You're on a 5-day streak! Keep going!", time: "Today", unread: true, confessionId: null }
  ],
  nextConfessionId: 3,
  activePostIndex: null,
  selectedReaction: null,   // reaction chosen in reply area before submitting

  // Registry of all usernames taken — enforces uniqueness
  takenUsernames: ["Anonymous #2841", "Anonymous #4421", "Anonymous #8832", "Anonymous #3310"]
};

// ===================== CONSTANTS =====================
var moodEmojis    = { Happy: "😊", Sad: "😢", Angry: "😤", Love: "🥰", Fear: "😰",Funny: "😅" };
var moodColors    = { Happy: "#f9a825", Sad: "#5c6bc0", Angry: "#e53935", Love: "#e91e63", Fear: "#43a047" };
var positiveMoods = ["Happy", "Love"];
var trendData     = {};
var avatarPalette = ["#7c6fef","#e91e63","#43a047","#f9a825","#5c6bc0","#e53935","#00acc1","#fb8c00"];

function avatarColor(name) {
  var h = 0;
  for (var i = 0; i < name.length; i++) h += name.charCodeAt(i);
  return avatarPalette[h % avatarPalette.length];
}

// ===================== TOKEN VALIDATION + DASHBOARD LOAD =====================
function validateTokenOrRedirect() {
  var token = localStorage.getItem("token");
  if (!token) {
    alert("Please login first");
    window.location.href = "login.html";
    return false;
  }
  return true;
}

function loadDashboardData() {
  var token = localStorage.getItem("token");
  if (!token) return;

  fetch("http://localhost:8080/api/dashboard/me", {
    method: "GET",
    headers: {
      "Authorization": "Bearer " + token
    }
  })
    .then(function(response) {
      if (!response.ok) {
        throw new Error("Failed to load dashboard");
      }
      return response.json();
    })
    .then(function(data) {
      renderDashboardData(data);
      console.log("here hererrerere seeeee");
      console.log(data);
    
    })
    .catch(function(error) {
      console.error("Error loading dashboard:", error);
    });
}

function renderDashboardData(data) {
  state.username = data.username || state.username;
  state.memberSince = data.memberSince || "Not available";

  if (data.recentConfessions && Array.isArray(data.recentConfessions)) {
    state.confessions = data.recentConfessions.map(function(confession) {
      var reactionMap = {};

      if (confession.reactions && Array.isArray(confession.reactions)) {
        confession.reactions.forEach(function(r) {
          var emoji = mapReactionTypeToEmoji(r.reactionType);
          reactionMap[emoji] = r.count;
        });
      }

      var commentList = [];
      if (confession.comments && Array.isArray(confession.comments)) {
        commentList = confession.comments.map(function(comment) {
          return {
            id: comment.id,
            author: comment.username || "Anonymous",
            time: getTimeAgo(comment.createdAt),
            text: comment.text,
            likes: 0,
            liked: false,
            reactions: {},
            userReaction: null
          };
        });
      }

      return {
        id: confession.id,
        mood: formatMood(confession.mood),
        cat: confession.category,
        time: getTimeAgo(confession.createdAt),
        text: confession.text,
        reactions: reactionMap,
        comments: commentList
      };
    });

    state.nextConfessionId = state.confessions.length + 1;
  }

  state.moodData = { Happy: 0, Sad: 0, Angry: 0, Love: 0, Fear: 0 };
  state.categories = {};

  state.confessions.forEach(function(confession) {
    if (state.moodData[confession.mood] !== undefined) {
      state.moodData[confession.mood]++;
    }

    if (!state.categories[confession.cat]) {
      state.categories[confession.cat] = 0;
    }
    state.categories[confession.cat]++;
  });

  document.getElementById("sidebarUsername").textContent = state.username;
  document.getElementById("profileName").textContent = state.username;

  var firstLetter = state.username ? state.username.charAt(0).toUpperCase() : "A";
  document.getElementById("sidebarAvatar").textContent = firstLetter;
  document.getElementById("profileAvatar").textContent = firstLetter;
  document.getElementById("replyAvatar").textContent = firstLetter;

  document.getElementById("memberSince").textContent = state.memberSince;
  document.getElementById("roMemberSince").textContent = state.memberSince;

  document.getElementById("confessionCount").textContent = data.totalConfessionsPosted || 0;
  document.getElementById("statConfessions").textContent = data.totalConfessionsPosted || 0;
  document.getElementById("statReactions").textContent = data.totalReactionsReceived || 0;
  document.getElementById("statComments").textContent = data.totalCommentsReceived || 0;
  document.getElementById("statMoods").textContent = data.moodTypesUsed || 0;

  renderProfile();
  renderDonut();
  updateTrendChart();
  renderCategoryBars();
  renderBalancePie();
  renderReflection();
  renderConfessions();
  renderJourney();
  renderNotifications();
}

function formatMood(mood) {
  if (!mood) return "";
  mood = mood.toLowerCase();

  if (mood === "happy") return "Happy";
  if (mood === "sad") return "Sad";
  if (mood === "angry") return "Angry";
  if (mood === "love") return "Love";
  if (mood === "fear") return "Fear";
  if (mood === "funny") return "Funny";

  return mood.charAt(0).toUpperCase() + mood.slice(1);
}

function mapReactionTypeToEmoji(reactionType) {
  if (reactionType === "FEEL_THIS") return "❤️";
  if (reactionType === "FUNNY") return "😂";
  if (reactionType === "STAY_STRONG") return "🤗";
  if (reactionType === "BEEN_THERE") return "✨";
  return "❤️";
}

function getTimeAgo(createdAt) {
  var now = new Date();
  var postTime = new Date(createdAt);
  var diffInSeconds = Math.floor((now - postTime) / 1000);

  if (diffInSeconds < 5) return "Just now";
  if (diffInSeconds < 60) return diffInSeconds + " sec ago";
  if (diffInSeconds < 3600) return Math.floor(diffInSeconds / 60) + " min ago";
  if (diffInSeconds < 86400) return Math.floor(diffInSeconds / 3600) + " hours ago";
  return Math.floor(diffInSeconds / 86400) + " days ago";
}

// ===================== INIT =====================
function init() {
  if (!validateTokenOrRedirect()) return;

  renderProfile();
  renderStats();
  renderDonut();
  updateTrendChart();
  renderCategoryBars();
  renderBalancePie();
  renderReflection();
  renderConfessions();
  renderJourney();
  renderNotifications();

  loadDashboardData();

  // Overlay background click closes modal
  document.querySelectorAll(".overlay").forEach(function(ov) {
    ov.addEventListener("click", function(e) {
      if (e.target === ov) {
        if (ov.id === "notifPageOverlay") closeNotifPage();
        else closeModal(ov.id);
      }
    });
  });

  // Close notif dropdown on outside click
  document.addEventListener("click", function(e) {
    var panel = document.getElementById("notifPanel");
    var btn   = document.getElementById("notifBtn");
    if (panel && btn && !panel.contains(e.target) && !btn.contains(e.target)) {
      panel.classList.remove("open");
    }
  });

  // ── Auto Online / Offline via Page Visibility API ──
  function setOnline() {
    state.status = "Online";
    renderStatusBadge();
  }
  function setOffline() {
    state.status = "Offline";
    renderStatusBadge();
  }

  document.addEventListener("visibilitychange", function() {
    if (document.visibilityState === "hidden") setOffline();
    else setOnline();
  });
  window.addEventListener("beforeunload", setOffline);
  window.addEventListener("focus",  setOnline);
  window.addEventListener("blur",   setOffline);
}

// ===================== PROFILE =====================
function renderProfile() {
  var initial = state.username.charAt(0).toUpperCase();
  document.getElementById("profileAvatar").textContent   = initial;
  document.getElementById("sidebarAvatar").textContent   = initial;
  document.getElementById("replyAvatar").textContent     = initial;
  document.getElementById("profileName").textContent     = state.username;
  document.getElementById("sidebarUsername").textContent = state.username;
  document.getElementById("memberSince").textContent     = state.memberSince;
  document.getElementById("streakCount").textContent     = state.streak;
  renderStatusBadge();
}

function renderStatusBadge() {
  var dot  = document.getElementById("statusDot");
  var txt  = document.getElementById("statusText");
  var isOn = state.status === "Online";
  dot.className   = "status-dot" + (isOn ? "" : " offline");
  txt.textContent = state.status;
}

// ===================== STATS =====================
function renderStats() {
  var totalConfessions = Object.values(state.moodData).reduce(function(a,b){return a+b;},0);
  document.getElementById("statConfessions").textContent = totalConfessions;
  document.getElementById("confessionCount").textContent = totalConfessions;

  // Reactions = all reaction counts across all confessions + all comment reactions
  var totalReactions = 0;
  state.confessions.forEach(function(c) {
    Object.values(c.reactions).forEach(function(v){ totalReactions += v; });
    c.comments.forEach(function(cm) {
      Object.values(cm.reactions).forEach(function(v){ totalReactions += v; });
    });
  });
  document.getElementById("statReactions").textContent =
    totalReactions >= 1000 ? (totalReactions/1000).toFixed(1)+"K" : totalReactions;

  // Comments = total comment count across all confessions
  var totalComments = 0;
  state.confessions.forEach(function(c){ totalComments += c.comments.length; });
  document.getElementById("statComments").textContent = totalComments;

  document.getElementById("statMoods").textContent = "5";
}

// ===================== DONUT CHART =====================
function renderDonut() {
  var canvas = document.getElementById("donutChart");
  var ctx    = canvas.getContext("2d");
  var data   = state.moodData;
  var total  = Object.values(data).reduce(function(a,b){return a+b;},0);

  if (total === 0) {
    ctx.clearRect(0,0,160,160);
    ctx.beginPath(); ctx.arc(80,80,65,0,Math.PI*2); ctx.fillStyle="#ececf8"; ctx.fill();
    ctx.beginPath(); ctx.arc(80,80,42,0,Math.PI*2); ctx.fillStyle="#ffffff"; ctx.fill();
    document.getElementById("topMoodEmoji").textContent  = "🎭";
    document.getElementById("topMoodPct").textContent    = "0%";
    document.getElementById("topMoodName").textContent   = "None";
    document.getElementById("footerTopMood").textContent = "None";
    document.getElementById("footerBalance").textContent = "0% Positive";
    document.getElementById("donutLegend").innerHTML     = "";
    return;
  }

  var moods = Object.keys(data);
  ctx.clearRect(0,0,160,160);
  var angle = -Math.PI/2;
  moods.forEach(function(m) {
    var slice = (data[m]/total)*Math.PI*2;
    ctx.beginPath(); ctx.moveTo(80,80); ctx.arc(80,80,65,angle,angle+slice); ctx.closePath();
    ctx.fillStyle = moodColors[m]; ctx.fill(); angle += slice;
  });
  ctx.beginPath(); ctx.arc(80,80,42,0,Math.PI*2); ctx.fillStyle="#ffffff"; ctx.fill();

  var topMood = moods.reduce(function(a,b){return data[a]>data[b]?a:b;});
  var topPct  = Math.round(data[topMood]/total*100);
  document.getElementById("topMoodEmoji").textContent  = moodEmojis[topMood];
  document.getElementById("topMoodPct").textContent    = topPct+"%";
  document.getElementById("topMoodName").textContent   = topMood;
  document.getElementById("footerTopMood").textContent = topMood+" "+moodEmojis[topMood];

  var posCount = moods.filter(function(m){return positiveMoods.indexOf(m)!==-1;})
                      .reduce(function(s,m){return s+data[m];},0);
  var posPct = Math.round(posCount/total*100);
  var balEl  = document.getElementById("footerBalance");
  balEl.textContent = posPct+"% Positive";
  balEl.style.color = posPct>=50 ? "var(--green)" : "var(--accent)";

  document.getElementById("donutLegend").innerHTML = moods.map(function(m){
    return '<div class="legend-item"><div class="legend-dot" style="background:'+moodColors[m]+'"></div>'+
      moodEmojis[m]+' '+m+' ('+Math.round(data[m]/total*100)+'%)</div>';
  }).join('');
}

// ===================== TREND CHART =====================
function generateTrendData(days) {
  var moods=Object.keys(moodColors), labels=[], today=new Date();
  for(var i=days-1;i>=0;i--){
    var d=new Date(today); d.setDate(d.getDate()-i);
    labels.push(days<=7 ? d.toLocaleDateString('en',{weekday:'short'}) : d.toLocaleDateString('en',{month:'short',day:'numeric'}));
  }
  var datasets={};
  moods.forEach(function(m){
    datasets[m]=[]; var val=5+Math.random()*15;
    for(var j=0;j<days;j++){val=Math.max(0,Math.min(30,val+(Math.random()-0.5)*8));datasets[m].push(Math.round(val));}
  });
  return {labels:labels,datasets:datasets};
}
function updateTrendChart(){trendData=generateTrendData(parseInt(document.getElementById("trendRange").value));drawLineChart();}
function drawLineChart(){
  var canvas=document.getElementById("lineChart"),ctx=canvas.getContext("2d");
  var labels=trendData.labels,datasets=trendData.datasets;
  var W=canvas.offsetWidth||420,H=180; canvas.width=W; canvas.height=H; ctx.clearRect(0,0,W,H);
  var pad={top:16,right:20,bottom:28,left:32},cw=W-pad.left-pad.right,ch=H-pad.top-pad.bottom;
  var moods=Object.keys(datasets),N=labels.length;
  ctx.strokeStyle="#ececf8"; ctx.lineWidth=1;
  for(var i=0;i<=4;i++){var y=pad.top+ch-(i/4)*ch;ctx.beginPath();ctx.moveTo(pad.left,y);ctx.lineTo(W-pad.right,y);ctx.stroke();ctx.fillStyle="#9e9eb5";ctx.font="700 10px Inter,sans-serif";ctx.textAlign="right";ctx.fillText(Math.round(i/4*30),pad.left-4,y+4);}
  var step=Math.max(1,Math.floor(N/7)); ctx.fillStyle="#9e9eb5"; ctx.textAlign="center"; ctx.font="700 10px Inter,sans-serif";
  labels.forEach(function(l,idx){if(idx%step===0||idx===N-1)ctx.fillText(l,pad.left+(idx/(N-1))*cw,H-4);});
  moods.forEach(function(m){ctx.beginPath();ctx.strokeStyle=moodColors[m];ctx.lineWidth=2.5;ctx.lineJoin="round";ctx.lineCap="round";datasets[m].forEach(function(v,idx){var x=pad.left+(idx/(N-1))*cw,y=pad.top+ch-(v/30)*ch;if(idx===0)ctx.moveTo(x,y);else ctx.lineTo(x,y);});ctx.stroke();});
  document.getElementById("trendLegend").innerHTML=moods.map(function(m){return '<div class="legend-item"><div class="legend-dot" style="background:'+moodColors[m]+'"></div>'+moodEmojis[m]+' '+m+'</div>';}).join('');
}

// ===================== CATEGORY BARS =====================
function renderCategoryBars(){
  var cats=state.categories,keys=Object.keys(cats),container=document.getElementById("categoryBars");
  if(!keys.length){container.innerHTML='<div style="color:var(--muted);font-size:0.85rem;text-align:center;padding:20px 0;">No categories yet.</div>';return;}
  var total=keys.reduce(function(s,k){return s+cats[k];},0);
  container.innerHTML=keys.map(function(cat){var pct=Math.round(cats[cat]/total*100);return '<div class="bar-row"><div class="bar-label"><span>'+cat+'</span><span style="color:var(--primary)">'+pct+'%</span></div><div class="bar-track"><div class="bar-fill" style="width:'+pct+'%"></div></div></div>';}).join('');
}

// ===================== BALANCE PIE =====================
function renderBalancePie(){
  var data=state.moodData,total=Object.values(data).reduce(function(a,b){return a+b;},0);
  var canvas=document.getElementById("balancePie"),ctx=canvas.getContext("2d");
  ctx.clearRect(0,0,120,120);
  if(!total){document.getElementById("positivePct").textContent="0%";document.getElementById("negativePct").textContent="0%";document.getElementById("posBar").style.width="0%";document.getElementById("negBar").style.width="0%";return;}
  var posCount=Object.entries(data).filter(function(e){return positiveMoods.indexOf(e[0])!==-1;}).reduce(function(s,e){return s+e[1];},0);
  var posPct=Math.round(posCount/total*100),negPct=100-posPct,start=-Math.PI/2;
  ctx.beginPath();ctx.moveTo(60,60);ctx.arc(60,60,55,start,start+Math.PI*2*posPct/100);ctx.closePath();ctx.fillStyle="#66bb6a";ctx.fill();
  ctx.beginPath();ctx.moveTo(60,60);ctx.arc(60,60,55,start+Math.PI*2*posPct/100,start+Math.PI*2);ctx.closePath();ctx.fillStyle="#f06292";ctx.fill();
  document.getElementById("positivePct").textContent=posPct+"%";
  document.getElementById("negativePct").textContent=negPct+"%";
  document.getElementById("posBar").style.width=posPct+"%";
  document.getElementById("negBar").style.width=negPct+"%";
  var quotes=['"Expressing emotions regularly helps improve emotional clarity."','"Sharing your feelings is a sign of strength, not weakness."','"Every emotion you feel is valid and worth acknowledging."'];
  document.getElementById("balanceQuote").textContent=quotes[Math.floor(Math.random()*quotes.length)];
}

// ===================== REFLECTION =====================
function renderReflection(){
  var entries=Object.entries(state.categories);
  if(!entries.length){document.getElementById("reflectionText").innerHTML="Start sharing and we'll reflect your emotional journey here. 💜";return;}
  var sorted=entries.slice().sort(function(a,b){return b[1]-a[1];});
  var top1=sorted[0][0],top2=sorted.length>1?sorted[1][0]:top1;
  document.getElementById("reflectionText").innerHTML='This month you shared many confessions related to <strong>'+top1+'</strong> and <strong>'+top2.toLowerCase()+' reflections</strong>. Many users experience similar emotional cycles. You\'re doing great by letting it out. 💜';
}

// ===================== CONFESSIONS LIST =====================
function renderConfessions(){
  var moodBg={Happy:"#fff8e1",Sad:"#e8eaf6",Angry:"#ffebee",Love:"#fce4ec",Fear:"#e8f5e9"};
  var moodTx={Happy:"#f57f17",Sad:"#3949ab",Angry:"#c62828",Love:"#ad1457",Fear:"#2e7d32"};
  var container=document.getElementById("confessionsList");
  if(!state.confessions.length){container.innerHTML='<div style="color:var(--muted);font-size:0.85rem;text-align:center;padding:32px 0;">No confessions yet. Share your first one! 💜</div>';return;}
  container.innerHTML=state.confessions.map(function(c,i){
    var reactHTML=Object.entries(c.reactions).map(function(r){return '<span class="reaction">'+r[0]+' '+r[1]+'</span>';}).join('');
    var cc=c.comments.length;
    return '<div class="confession-card" id="confession-'+i+'" onclick="openPostView('+i+')">' +
      '<div class="confession-header">' +
        '<span class="mood-chip" style="background:'+moodBg[c.mood]+';color:'+moodTx[c.mood]+'">'+moodEmojis[c.mood]+' '+c.mood+'</span>' +
        '<span class="cat-chip">'+c.cat+'</span>' +
        '<span class="confession-time">'+c.time+'</span>' +
        '<span class="click-hint">👆 View comments</span>' +
        '<button class="delete-btn" onclick="event.stopPropagation();deleteConfession('+i+')">🗑️ Delete</button>' +
      '</div>' +
      '<div class="confession-text">'+c.text+'</div>' +
      '<div class="reactions">'+reactHTML+'<span class="comment-count-badge">💬 '+cc+(cc===1?' comment':' comments')+'</span></div>' +
      '</div>';
  }).join('');
}

// ===================== POST VIEW MODAL =====================
function openPostView(index, scrollToComments){
  var c=state.confessions[index]; if(!c)return;
  state.activePostIndex=index;
  var moodBg={Happy:"#fff8e1",Sad:"#e8eaf6",Angry:"#ffebee",Love:"#fce4ec",Fear:"#e8f5e9"};
  var moodTx={Happy:"#f57f17",Sad:"#3949ab",Angry:"#c62828",Love:"#ad1457",Fear:"#2e7d32"};
  var reactHTML=Object.entries(c.reactions).map(function(r){return '<span class="reaction">'+r[0]+' '+r[1]+'</span>';}).join('');
  document.getElementById("postModalContent").innerHTML=
    '<div class="post-modal-post-header">' +
      '<span class="mood-chip" style="background:'+moodBg[c.mood]+';color:'+moodTx[c.mood]+'">'+moodEmojis[c.mood]+' '+c.mood+'</span>' +
      '<span class="cat-chip">'+c.cat+'</span>' +
      '<span class="confession-time">'+c.time+'</span>' +
    '</div>' +
    '<div class="post-modal-post-text">'+c.text+'</div>' +
    '<div class="post-modal-post-reactions">'+reactHTML+'</div>';
  refreshPostComments(index);
  document.getElementById("replyText").value="";
  document.getElementById("replyCharCount").textContent="0";
  state.selectedReaction=null;
  document.querySelectorAll(".react-pick-btn").forEach(function(b){b.classList.remove("selected");});
  document.getElementById("selectedReactionPreview").textContent="";
  document.getElementById("postViewModal").classList.add("open");
  if(scrollToComments){
    setTimeout(function(){var el=document.getElementById("postModalComments");if(el)el.scrollIntoView({behavior:"smooth"});},300);
  }
}

function refreshPostComments(index){
  var c=state.confessions[index];
  var cc=c.comments.length;
  document.getElementById("postCommentCount").textContent=cc===0?"No comments yet":cc+(cc===1?" Comment":" Comments");
  var container=document.getElementById("postModalComments");
  if(!cc){container.innerHTML='<div class="no-comments">Be the first to comment! 💬<br><span style="font-size:0.75rem;color:var(--muted);margin-top:6px;display:block;">All comments are anonymous.</span></div>';return;}
  container.innerHTML=c.comments.map(function(comment,ci){
    var color=avatarColor(comment.author);
    var reactBubbles=Object.entries(comment.reactions).map(function(r){
      var isReacted=comment.userReaction===r[0];
      return '<span class="comment-react-bubble'+(isReacted?' reacted':'') +'" onclick="toggleCommentReaction('+index+','+ci+',\''+r[0]+'\')">'+r[0]+' '+r[1]+'</span>';
    }).join('');
    var reactionPreview=comment.selectedReactionEmoji?'<span style="font-size:1rem;margin-left:4px;">'+comment.selectedReactionEmoji+'</span>':'';
    return '<div class="comment-item">' +
      '<div class="comment-avatar" style="background:'+color+'">'+comment.author.charAt(comment.author.length-1)+'</div>' +
      '<div class="comment-body">' +
        '<div class="comment-author">'+comment.author+'<span class="comment-time">'+comment.time+'</span></div>' +
        '<div class="comment-text">'+comment.text+(reactionPreview?'&nbsp;'+reactionPreview:'')+'</div>' +
        (reactBubbles?'<div class="comment-reactions">'+reactBubbles+'</div>':'') +
        '<div class="comment-actions">' +
          '<button class="comment-like-btn'+(comment.liked?' liked':'')+'" onclick="toggleLikeComment('+index+','+ci+')">❤️ '+comment.likes+'</button>' +
        '</div>' +
      '</div>' +
      '</div>';
  }).join('');
}

function toggleCommentReaction(postIndex, commentIndex, emoji){
  var comment=state.confessions[postIndex].comments[commentIndex];
  if(comment.userReaction===emoji){
    comment.reactions[emoji]--;
    if(comment.reactions[emoji]<=0) delete comment.reactions[emoji];
    comment.userReaction=null;
  } else {
    if(comment.userReaction){
      comment.reactions[comment.userReaction]--;
      if(comment.reactions[comment.userReaction]<=0) delete comment.reactions[comment.userReaction];
    }
    comment.userReaction=emoji;
    if(!comment.reactions[emoji]) comment.reactions[emoji]=0;
    comment.reactions[emoji]++;
  }
  renderStats();
  refreshPostComments(postIndex);
}

function toggleLikeComment(postIndex, commentIndex){
  var comment=state.confessions[postIndex].comments[commentIndex];
  comment.liked=!comment.liked;
  comment.likes+=comment.liked?1:-1;
  refreshPostComments(postIndex);
}

// ===================== REACTION PICKER (reply input) =====================
function pickReaction(emoji){
  if(state.selectedReaction===emoji){
    state.selectedReaction=null;
    document.querySelectorAll(".react-pick-btn").forEach(function(b){b.classList.remove("selected");});
    document.getElementById("selectedReactionPreview").textContent="";
  } else {
    state.selectedReaction=emoji;
    document.querySelectorAll(".react-pick-btn").forEach(function(b){
      b.classList.toggle("selected", b.title===emojiTitle(emoji));
    });
    document.getElementById("selectedReactionPreview").textContent="→ "+emoji;
  }
}
function emojiTitle(emoji){
  var map={"❤️":"Love","😢":"Sad","😂":"Haha","😮":"Wow","🤗":"Support","🔥":"Fire"};
  return map[emoji]||"";
}

// ===================== SUBMIT COMMENT =====================
function submitComment(){
  var text=document.getElementById("replyText").value.trim();
  if(!text){alert("Please write a comment first! 💬");return;}
  if(text.length>300){alert("Comment must be under 300 characters.");return;}
  var index=state.activePostIndex;
  if(index===null||index===undefined)return;
  var c=state.confessions[index];

  var reactionToAttach=state.selectedReaction;

  var newComment={
    id: Date.now(),
    author: state.username,
    time: "Just now",
    text: text,
    likes: 0,
    liked: false,
    reactions: {},
    userReaction: null,
    selectedReactionEmoji: reactionToAttach
  };

  c.comments.push(newComment);

  document.getElementById("replyText").value="";
  document.getElementById("replyCharCount").textContent="0";
  state.selectedReaction=null;
  document.querySelectorAll(".react-pick-btn").forEach(function(b){b.classList.remove("selected");});
  document.getElementById("selectedReactionPreview").textContent="";

  refreshPostComments(index);
  renderStats();
  renderConfessions();
  renderNotifications();

  setTimeout(function(){var el=document.getElementById("postModalComments");if(el)el.scrollTop=el.scrollHeight;},60);
}

function updateReplyCount(){document.getElementById("replyCharCount").textContent=document.getElementById("replyText").value.length;}

// ===================== DELETE CONFESSION =====================
function deleteConfession(index){
  var removed=state.confessions[index];
  if(state.moodData[removed.mood]>0) state.moodData[removed.mood]--;
  if(state.categories[removed.cat]){state.categories[removed.cat]--;if(state.categories[removed.cat]<=0)delete state.categories[removed.cat];}
  state.confessions.splice(index,1);
  state.notifications.unshift({icon:"🗑️",text:"You deleted a "+removed.mood+" confession from "+removed.cat,time:"Just now",unread:true,confessionId:null});
  renderStats();renderDonut();renderCategoryBars();renderBalancePie();renderReflection();renderConfessions();renderJourney();renderNotifications();updateTrendChart();
}

// ===================== JOURNEY =====================
function renderJourney(){
  var container=document.getElementById("journeyList");
  var items=state.confessions.map(function(c,i){return{date:i===0?"Today":"Yesterday",cat:c.cat,preview:c.text.substring(0,22)+"...",emoji:moodEmojis[c.mood]};});
  items.push({date:"Mar 10",cat:"School",preview:"Exam anxiety hit...",emoji:"😰"},{date:"Mar 8",cat:"Family",preview:"Miss home so much...",emoji:"😢"});
  container.innerHTML=items.map(function(j){return '<div class="journey-item"><div class="journey-dot">'+j.emoji+'</div><div class="journey-info"><div class="journey-date">'+j.date+'</div><div class="journey-cat">'+j.cat+'</div><div class="journey-preview">'+j.preview+'</div></div></div>';}).join('');
}

// ===================== NOTIFICATIONS =====================
function renderNotifications(){
  var badge=document.getElementById("notifBadge");
  var unread=state.notifications.filter(function(n){return n.unread;}).length;
  badge.textContent=unread; badge.style.display=unread>0?"flex":"none";

  var html=state.notifications.map(function(n,i){
    var hasLink=n.confessionId!==null&&n.confessionId!==undefined;
    var clickFn=hasLink?"openPostFromNotif("+i+")":"readNotif("+i+")";
    var border=hasLink?"border-left:3px solid var(--primary);":"";
    return '<div class="notif-item" style="'+border+'" onclick="'+clickFn+'">' +
      '<div class="notif-item-icon">'+n.icon+'</div>' +
      '<div style="flex:1"><div class="notif-item-text">'+n.text+'</div>' +
      '<div class="notif-item-time">'+n.time+(hasLink?' <span style="color:var(--primary);font-size:0.65rem;">· Tap to view</span>':'')+'</div></div>' +
      (n.unread?'<div class="notif-unread"></div>':'') +
      '</div>';
  }).join('');
  if(!state.notifications.length) html='<div style="padding:24px;text-align:center;color:var(--muted);font-size:0.82rem;">No notifications yet.</div>';

  document.getElementById("notifList").innerHTML=html;
  var pl=document.getElementById("notifPageList");
  if(pl) pl.innerHTML=html||'<div class="notif-page-empty">No notifications yet. 🌙</div>';
}

function openPostFromNotif(i){
  var notif=state.notifications[i]; notif.unread=false; renderNotifications();
  var postIndex=null;
  for(var j=0;j<state.confessions.length;j++){if(state.confessions[j].id===notif.confessionId){postIndex=j;break;}}
  document.getElementById("notifPanel").classList.remove("open");
  closeNotifPage();
  if(postIndex!==null) openPostView(postIndex,true);
}

function readNotif(i){state.notifications[i].unread=false;renderNotifications();}
function markAllRead(){state.notifications.forEach(function(n){n.unread=false;});renderNotifications();}
function toggleNotif(){document.getElementById("notifPanel").classList.toggle("open");}
function openNotifPage(){renderNotifications();document.getElementById("notifPageOverlay").classList.add("open");}
function closeNotifPage(){document.getElementById("notifPageOverlay").classList.remove("open");}

// ===================== MODALS =====================
function openModal(id){
  document.getElementById(id).classList.add("open");
  if(id==="editProfileModal"){
    document.getElementById("editName").value=state.username;
    document.getElementById("editName").classList.remove("error");
    document.getElementById("usernameError").style.display="none";
    document.getElementById("roMemberSince").textContent=state.memberSince;
    document.getElementById("roStatus").textContent=state.status;
  }
}
function closeModal(id){
  document.getElementById(id).classList.remove("open");
  if(id==="postViewModal") state.activePostIndex=null;
}
function clearUsernameError(){
  document.getElementById("editName").classList.remove("error");
  document.getElementById("usernameError").style.display="none";
}

// ===================== SAVE PROFILE =====================
function saveProfile(){
  var newName=document.getElementById("editName").value.trim();
  var errEl=document.getElementById("usernameError");
  var input=document.getElementById("editName");

  if(!newName){
    input.classList.add("error");
    errEl.textContent="Username cannot be empty.";
    errEl.style.display="block";
    return;
  }

  var isTaken=state.takenUsernames.some(function(u){
    return u.toLowerCase()===newName.toLowerCase() && u.toLowerCase()!==state.username.toLowerCase();
  });

  if(isTaken){
    input.classList.add("error");
    errEl.textContent="❌ That username is already taken. Please choose a different one.";
    errEl.style.display="block";
    return;
  }

  var oldIndex=state.takenUsernames.indexOf(state.username);
  if(oldIndex!==-1) state.takenUsernames.splice(oldIndex,1);
  state.takenUsernames.push(newName);

  var oldName = state.username;
  state.username = newName;

  state.confessions.forEach(function(confession) {
    confession.comments.forEach(function(comment) {
      if (comment.author === oldName) {
        comment.author = newName;
      }
    });
  });

  state.notifications.forEach(function(notif) {
    if (notif.text.indexOf(oldName) !== -1) {
      notif.text = notif.text.split(oldName).join(newName);
    }
  });

  renderProfile();
  renderConfessions();
  renderNotifications();

  if (state.activePostIndex !== null) {
    refreshPostComments(state.activePostIndex);
  }

  closeModal("editProfileModal");
}

// ===================== LOGOUT =====================
function confirmLogout(){
  closeModal("logoutModal");
  localStorage.removeItem("token");
  state.status="Offline";
  renderStatusBadge();
  document.querySelector(".sidebar").style.display="none";
  document.querySelector(".main").style.display="none";
  document.getElementById("loggedOutScreen").style.display="flex";
}

function loginAgain(){
  window.location.href = "login.html";
}

// ===================== CONFESSION MODAL =====================
function selectMood(el){document.querySelectorAll(".mood-opt").forEach(function(o){o.classList.remove("selected");});el.classList.add("selected");}
function updateCharCount(){document.getElementById("charCount").textContent=document.getElementById("confessionText").value.length;}

function submitConfession(){
  var text=document.getElementById("confessionText").value.trim();
  if(!text){alert("Please write something to confess! 💜");return;}
  var mood=document.querySelector(".mood-opt.selected").getAttribute("data-mood");
  var cat=document.getElementById("confessionCategory").value;
  state.confessions.unshift({id:state.nextConfessionId++,mood:mood,cat:cat,time:"Just now",text:'"'+text+'"',reactions:{"❤️":0},comments:[]});
  state.moodData[mood]++;
  if(!state.categories[cat])state.categories[cat]=0; state.categories[cat]++;
  document.getElementById("confessionText").value=""; document.getElementById("charCount").textContent="0";
  closeModal("confessionModal");
  state.notifications.unshift({icon:"🎭",text:"You posted a new "+mood+" confession in "+cat,time:"Just now",unread:true,confessionId:state.nextConfessionId-1});
  renderStats();renderDonut();renderCategoryBars();renderBalancePie();renderReflection();renderConfessions();renderJourney();renderNotifications();updateTrendChart();
}

// ===================== UTILS =====================
function smoothScroll(id){var el=document.getElementById(id);if(el)el.scrollIntoView({behavior:"smooth"});}

// ===================== START =====================
init();
window.addEventListener("resize", drawLineChart);