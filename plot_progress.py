import pandas as pd
import matplotlib.pyplot as plt


files = [("ILS1Best.csv", "ILS"), ("SA1Best.csv", "SA")]   # use SAT1Best.csv for the timed SA

fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(11, 4))

for fname, label in files:
    d = pd.read_csv(fname, skipinitialspace=True)
    d.columns = [c.strip() for c in d.columns]          # header has stray spaces
    t_ms = d["time"] / 1e6                               # nanoseconds -> milliseconds
    ax1.step(t_ms, d["value"], where="post", label=label)
    ax2.step(d["iteration"], d["value"], where="post", label=label)

ax1.set_xscale("symlog", linthresh=0.01)                 # symlog handles time ~ 0
ax1.set_xlabel("time (ms)")
ax2.set_xscale("symlog", linthresh=1)                    # symlog handles iteration 0
ax2.set_xlabel("iteration")

for ax in (ax1, ax2):
    ax.axhline(595, color="grey", linestyle="--", linewidth=0.8)   # optimum
    ax.set_ylabel("best value so far")
    ax.legend()

fig.suptitle("Progress of one run (seed 1)")
fig.tight_layout()
fig.savefig("progress.png", dpi=150)
plt.show()