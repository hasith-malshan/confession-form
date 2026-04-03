/* ─────────────────────────────────────────
   ANONCONFESS — script.js
   Revised for your current backend
   ───────────────────────────────────────── */

/* ── Nav scroll class ── */
const nav = document.getElementById('nav');
if (nav) {
  window.addEventListener('scroll', () => {
    nav.classList.toggle('scrolled', window.scrollY > 20);
  });
}

/* ── Hamburger (mobile) ── */
const hamburger = document.getElementById('hamburger');
if (hamburger) {
  hamburger.addEventListener('click', () => {
    const links = document.querySelector('.nav__links');
    const actions = document.querySelector('.nav__actions');
    if (!links || !actions) return;

    const open = links.style.display === 'flex';
    links.style.cssText   = open ? '' : 'display:flex;flex-direction:column;position:fixed;top:68px;left:0;right:0;background:#fff;padding:1.5rem 5vw;gap:1.25rem;border-bottom:1px solid #edebf4;z-index:99';
    actions.style.cssText = open ? '' : 'display:flex;flex-direction:column;position:fixed;top:calc(68px + 9rem);left:0;right:0;background:#fff;padding:0 5vw 1.5rem;gap:.75rem;z-index:99';
  });
}

/* ── Scroll Reveal ── */
const revealEls = document.querySelectorAll('.reveal');
if (revealEls.length) {
  const io = new IntersectionObserver(
    (entries) => entries.forEach(e => { if (e.isIntersecting) { e.target.classList.add('visible'); io.unobserve(e.target); } }),
    { threshold: 0.12 }
  );
  revealEls.forEach(el => io.observe(el));
}

/* ── Floating feed cards ── */
document.querySelectorAll('.feed-card[data-float]').forEach((card) => {
  const i = parseInt(card.dataset.float, 10);
  const amp = 6 + i * 3;
  const spd = 3.2 + i * 0.7;
  const off = i * 1.1;
  let start = null;
  const tick = (ts) => {
    if (!start) start = ts;
    const t = (ts - start) / 1000;
    const y = Math.sin((t + off) * (2 * Math.PI / spd)) * amp;
    const x = (i === 0) ? 24 + Math.sin((t + off) * (2 * Math.PI / (spd * 1.4))) * 4 : 0;
    card.style.transform = `translateY(${y}px) translateX(${x}px)`;
    requestAnimationFrame(tick);
  };
  requestAnimationFrame(tick);
});

/* ── Backend mapping helpers ── */
const MOOD_REVERSE = {
  Love: 'love',
  Happy: 'happy',
  Sad: 'sad',
  Angry: 'angry',
  Fear: 'fear',
  Funny: 'funny',
  Embarrassing: 'embarrassing',
  Overthinking: 'overthinking'
};

const MOOD_COLORS_2 = {
  Love: '#FF6B9D',
  Happy: '#FFD166',
  Sad: '#5BC4F5',
  Fear: '#A78BFA',
  Angry: '#FF8C60',
  Funny: '#f97316',
  Embarrassing: '#ec4899',
  Overthinking: '#06b6d4'
};

function getReactionTotal(reactions) {
  if (!reactions || !Array.isArray(reactions)) return 0;
  return reactions.reduce((sum, reaction) => sum + (reaction.count || 0), 0);
}

/* ── Trending / recent confessions grid ── */
async function loadTrendingConfessions() {
  try {
    const response = await fetch('http://localhost:8080/api/confessions?page=0&size=6&sort=createdAt,desc');
    const json = await response.json();

    if (!json.content || !json.content.length) return;

    const grid = document.getElementById('confessions-grid');
    if (!grid) return;

    grid.innerHTML = json.content.map((post, idx) => {
      const mood = MOOD_REVERSE[post.mood] || (post.mood ? post.mood.toLowerCase() : 'love');
      const interactions = getReactionTotal(post.reactions) + (post.commentCount || 0);

      return `
        <div class="confession-card" style="animation-delay:${idx * 0.08}s">
          <div class="confession-card__meta">
            <div style="display:flex;gap:.4rem;flex-wrap:wrap">
              <span class="tag tag--${mood}">${post.mood}</span>
            </div>
            <span style="font-size:.72rem;color:var(--fg-3)">${interactions} interactions</span>
          </div>
          <p>"${post.text.substring(0, 120)}${post.text.length > 120 ? '...' : ''}"</p>
          <div class="confession-card__footer">
            <span>${post.name || 'Anonymous'}</span>
            <div class="conf-actions">
              <span title="Reactions">❤️ ${getReactionTotal(post.reactions)}</span>
              <span title="Comments">💬 ${post.commentCount || 0}</span>
            </div>
          </div>
        </div>
      `;
    }).join('');
  } catch (err) {
    console.error('Failed to load trending:', err);
  }
}

loadTrendingConfessions();

async function loadHeroFloatingCards() {
  try {
    const response = await fetch('http://localhost:8080/api/confessions?page=0&size=3&sort=createdAt,desc');
    const json = await response.json();

    if (!json.content || !json.content.length) return;

    const cards = document.querySelectorAll('.feed-card[data-float]');
    json.content.slice(0, 3).forEach((post, i) => {
      const card = cards[i];
      if (!card) return;

      const mood = MOOD_REVERSE[post.mood] || 'love';
      const preview = post.text.substring(0, 80) + (post.text.length > 80 ? '...' : '');
      const reactions = getReactionTotal(post.reactions);

      const header = card.querySelector('.feed-card__header');
      const p = card.querySelector('p');
      const footer = card.querySelector('.feed-card__footer');

      if (header) {
        header.innerHTML = `
          <span class="tag tag--${mood}">${post.mood}</span>
          <span class="feed-card__time">Just now</span>
        `;
      }

      if (p) {
        p.textContent = `"${preview}"`;
      }

      if (footer) {
        footer.innerHTML = `
          <span class="anon">${post.name || 'Anonymous'}</span>
          <span class="heart-count">♥ ${reactions}</span>
        `;
      }
    });
  } catch(err) {
    console.error('Failed to load hero cards:', err);
  }
}

loadHeroFloatingCards();

/* ── Emotional Pulse Donut Chart ── */
async function drawPulse() {
  const canvas = document.getElementById('pulseChart');
  if (!canvas) return;

  const ctx = canvas.getContext('2d');
  const W = canvas.width, H = canvas.height;
  const cx = W / 2, cy = H / 2;
  const rOut = W / 2 - 8;
  const rIn  = rOut * 0.52;

  let data = [
    { label: 'Love', value: 34, color: '#FF6B9D' },
    { label: 'Happy', value: 22, color: '#FFD166' },
    { label: 'Sad', value: 20, color: '#5BC4F5' },
    { label: 'Fear', value: 14, color: '#A78BFA' },
    { label: 'Angry', value: 10, color: '#FF8C60' }
  ];

  try {
    const response = await fetch('http://localhost:8080/api/confessions/mood-distribution/today');
    const json = await response.json();

    if (Array.isArray(json) && json.length) {
      data = json.map(item => ({
        label: item.mood,
        value: Math.round(item.percentage),
        color: MOOD_COLORS_2[item.mood] || '#8b5cf6'
      }));
    }
  } catch(e) {
    console.error('Failed to load pulse mood data:', e);
  }

  const total = data.reduce((s, d) => s + d.value, 0) || 1;
  const gap = 0.025;
  const duration = 900;
  const start = performance.now();

  function draw(now) {
    const progress = Math.min((now - start) / duration, 1);
    const ease = 1 - Math.pow(1 - progress, 3);

    ctx.clearRect(0, 0, W, H);
    let angle = -Math.PI / 2;

    data.forEach(d => {
      const slice = (d.value / total) * 2 * Math.PI * ease;
      ctx.beginPath();
      ctx.moveTo(cx, cy);
      ctx.arc(cx, cy, rOut, angle + gap, angle + slice - gap);
      ctx.lineTo(cx, cy);
      ctx.closePath();
      ctx.fillStyle = d.color;
      ctx.fill();
      angle += slice;
    });

    ctx.beginPath();
    ctx.arc(cx, cy, rIn, 0, 2 * Math.PI);
    ctx.fillStyle = '#fff';
    ctx.fill();

    if (ease > 0.6 && data.length) {
      const top = [...data].sort((a,b) => b.value - a.value)[0];
      ctx.fillStyle = '#1A1523';
      ctx.font = `bold ${Math.round(W * 0.16)}px 'Playfair Display', serif`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillText(top.label, cx, cy - 10);

      ctx.font = `${Math.round(W * 0.09)}px 'DM Sans', sans-serif`;
      ctx.fillStyle = '#9390A0';
      ctx.fillText('dominant', cx, cy + 16);
    }

    if (progress < 1) requestAnimationFrame(draw);
  }

  requestAnimationFrame(draw);
}

const pulseSection = document.querySelector('.pulse');
if (pulseSection) {
  const pulseIO = new IntersectionObserver(entries => {
    if (entries[0].isIntersecting) { drawPulse(); pulseIO.disconnect(); }
  }, { threshold: 0.3 });
  pulseIO.observe(pulseSection);
}

/* ── Live activity ticker ── */
async function loadActivityFeed() {
  const feed = document.getElementById('activity-feed');
  if (!feed) return;

  try {
    const response = await fetch('http://localhost:8080/api/confessions?page=0&size=5&sort=createdAt,desc');
    const json = await response.json();
    if (!json.content || !json.content.length) return;

    const TIMES = ['Just now', '1m ago', '2m ago', '5m ago', '10m ago'];

    feed.innerHTML = '';
    json.content.forEach((post, i) => {
      const li = document.createElement('li');
      const preview = post.text.substring(0, 50) + (post.text.length > 50 ? '...' : '');
      li.innerHTML = `<span class="activity-name">${post.name || 'Anonymous'}</span> posted <strong>"${preview}"</strong> <span class="activity-time">${TIMES[i] || '10m ago'}</span>`;
      li.style.opacity = '0';
      li.style.transform = 'translateX(-12px)';
      feed.appendChild(li);
      requestAnimationFrame(() => {
        li.style.transition = 'opacity .4s ease, transform .4s ease';
        li.style.opacity = '1';
        li.style.transform = 'none';
      });
    });
  } catch(e) {
    console.error('Activity feed failed:', e);
  }
}

loadActivityFeed();
setInterval(loadActivityFeed, 30000);

/* ── User count ── */
async function loadUserCount() {
  try {
    const response = await fetch('http://localhost:8080/api/user/count');
    const json = await response.json();

    // supports both old style {success,data} and direct number
    let count = null;

    if (typeof json === 'number') {
      count = json;
    } else if (json && json.success && typeof json.data === 'number') {
      count = json.data;
    } else if (json && typeof json.count === 'number') {
      count = json.count;
    }

    if (count !== null) {
      const el = document.getElementById('userCount');
      if (el) el.textContent = count.toLocaleString() + '+';
    }
  } catch(e) {
    console.error('User count failed:', e);
  }
}

loadUserCount();


