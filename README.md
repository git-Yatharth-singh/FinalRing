# Final Ring 🎯

Final Ring is a real-time multiplayer arena game backend built with **Java and Spring Boot**.

The project is being developed incrementally to explore how a backend evolves from a simple REST application into a **real-time, scalable, distributed, and fault-tolerant multiplayer platform**.

The focus is not just on using different technologies, but on understanding **why each technology becomes useful as the system grows**.

---

# 🎮 Game Concept

Final Ring is currently designed around a short free-for-all arena match.

### Match Rules

- Minimum players: **2**
- Maximum players: **6**
- Match duration: **5 minutes**
- Players respawn after being killed
- Players can continue fighting throughout the match
- The player with the most kills when the timer ends wins
- If two players have the same number of kills, the player with fewer deaths ranks higher
- If both kills and deaths are equal, the player who appears first in the sorted result receives the higher rank
- Only the match creator can start the match

### Example

```text
5 Minute Match

Player A → 18 kills / 7 deaths
Player B → 15 kills / 4 deaths
Player C → 15 kills / 8 deaths
Player D → 10 kills / 6 deaths

Final Results

Rank 1 → Player A
Rank 2 → Player B
Rank 3 → Player C
Rank 4 → Player D
