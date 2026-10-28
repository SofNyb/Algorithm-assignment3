import csv
import matplotlib.pyplot as plt

with open("Summary.csv", newline="") as f:
    reader = csv.DictReader(f)
    if reader.fieldnames is None:
        raise ValueError("Summary.csv has no header row.")
    header_map = {h.strip().lower(): h for h in reader.fieldnames if h is not None}
    algorithm_col = header_map.get("algorithm")
    if algorithm_col is None:
        raise KeyError("Summary.csv must include an 'algorithm' column.")
    rows = list(reader)

names = [r[algorithm_col] for r in rows]
colors = ["#2a78d6", "#eb6834", "#1baf7a"]

panels = [
    ("Solution value", "meanValue", "stdValue"),
    ("Iteration where best was found", "meanBestIter", "stdBestIter"),
    ("Time to best solution (ms)", "meanTimeToBestMs", "stdTimeToBestMs"),
    ("Total run time (ms)", "meanTotalMs", "stdTotalMs"),
]

fig, axs = plt.subplots(1, 4, figsize=(15, 4.2))
for ax, (title, mcol, scol) in zip(axs, panels):
    mean = [float(r[mcol]) for r in rows]
    std = [float(r[scol]) for r in rows]

    ax.bar(names, mean, color=colors, width=0.55)
    ax.errorbar(names, mean, yerr=std, fmt="none", ecolor="black", capsize=4, lw=1.5)

    for i, (m, s) in enumerate(zip(mean, std)):          # value label above each bar
        ax.text(i, m + s + max(mean) * 0.03, f"{m:.1f}" if m >= 10 else f"{m:.2f}",
                ha="center", fontsize=9)

    ax.set_title(title, fontsize=11, loc="left")
    ax.set_ylim(0, max(m + s for m, s in zip(mean, std)) * 1.18)
    ax.grid(axis="y", color="#e5e4e0", lw=0.8)
    ax.set_axisbelow(True)
    for side in ("top", "right", "left"):
        ax.spines[side].set_visible(False)
    ax.tick_params(length=0, labelsize=9)

fig.suptitle("Hill climber vs ILS vs simulated annealing, 30 seeds each "
             "(bars = mean, whiskers = ±1 standard deviation)", fontsize=11, x=0.01, ha="left")
fig.tight_layout(rect=(0, 0, 1, 0.93))
fig.savefig("comparison.png", dpi=150)