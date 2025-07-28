
# 🔐 SecretFinder HASHIRA

A Java program that reconstructs a hidden secret using **Shamir's Secret Sharing** technique. It parses a JSON input with base-encoded polynomial shares and uses **Lagrange Interpolation** over all valid share combinations to recover the correct secret — even when some shares are tampered or incorrect.

---

## 📂 Project Structure

```
SecretFinder/
├── src/
│   └── SecretFinder.java
├── lib/
│   └── json-20230618.jar
├── testcase1.json
|-- testcase2.json
└── README.md
```

---

## 📦 Features

- Parses shares in various bases (binary, octal, hex, etc.) from a JSON file.
- Converts base values into `BigInteger`.
- Reconstructs the secret using **Lagrange interpolation** on all `k`-combinations.
- Handles incorrect or tampered shares by finding the **most common** reconstructed result.

---

## 🧪 Sample JSON Input Format

```json
{
  "keys": {
    "n": 5,
    "k": 3
  },
  "1": { "base": "10", "value": "12345" },
  "2": { "base": "16", "value": "2a3f" },
  "3": { "base": "2", "value": "1101" },
  "4": { "base": "8", "value": "75432" },
  "5": { "base": "3", "value": "102201" }
}
```

---

## 🚀 How to Run

### 🛠 Prerequisites

- Java 8 or higher
- IntelliJ IDEA (or any Java IDE)
- [org.json JAR file](https://repo1.maven.org/maven2/org/json/json/) (`json-20230618.jar` or newer)

### ✅ Setup in IntelliJ

1. Open IntelliJ and create a new Java Project.
2. Put `SecretFinder.java` in `src/`.
3. Put the JSON file (`testcase2.json`) in `src/` or root.
4. Download `json-20230618.jar` and add it to the project:
   - Right-click project → **Open Module Settings** → **Libraries** → `+` → Add the `.jar`.
5. Run `SecretFinder.main()`.

### 🔧 Change File Path

In `main()`:

```java
String filePath = "src/testcase2.json"; // Update if your file is elsewhere
```

---

## 📤 Output

```
Secret: 452130762305406606626 (appeared 20 times)
```

This means the most likely valid secret was reconstructed **20 times** from different combinations.

---

## 🧠 Concepts Used

- Lagrange Polynomial Interpolation
- BigInteger arithmetic for large values
- JSON parsing with `org.json`
- Combinatorics (`k`-combinations out of `n`)
- Majority voting to correct tampered shares

