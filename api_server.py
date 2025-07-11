from fastapi import FastAPI
import yfinance as yf
import math
import pandas as pd
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"], allow_methods=["*"], allow_headers=["*"]
)

def clean_df(df: pd.DataFrame):
    # Fill NaN or None with 0.0 only for needed fields
    for field in ['bid', 'ask', 'impliedVolatility', 'strike']:
        df[field] = df[field].apply(lambda x: 0.0 if x is None or (isinstance(x, float) and math.isnan(x)) else x)
    return df

@app.get("/options/{ticker}")
def get_options(ticker: str):
    try:
        stock = yf.Ticker(ticker)
        expiries = stock.options
        if not expiries:
            return {"calls": [], "puts": []}

        opt_chain = stock.option_chain(expiries[0])  # fetch first expiry
        calls = clean_df(opt_chain.calls.head(10)).to_dict(orient="records")
        puts = clean_df(opt_chain.puts.head(10)).to_dict(orient="records")
        

        return {"calls": calls, "puts": puts}
    except Exception as e:
        return {"error": str(e), "calls": [], "puts": []}
