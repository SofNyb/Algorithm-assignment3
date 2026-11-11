import pandas as pd
from pathlib import Path


def main() -> None:
    data_file = Path(__file__).with_name("NeighbourRuns.csv")
    data = pd.read_csv(data_file)
    hits = data.assign(hit=data["value"] >= 595)
    print(hits.groupby("algorithm")["hit"].sum())


if __name__ == "__main__":
    main()