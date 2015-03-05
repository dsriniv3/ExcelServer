package excel;

import java.util.HashMap;

public class Functions {
	HashMap<String, Integer> functionMap;
	public static final String[] FUNCTION_VALUES = new String[] {
        "ABS",         "ACCRINT",      "ACCRINTM",    "ACOS",      "ACOSH",
        "ADDRESS",     "AMORDEGRC",    "AMORLINC",    "AND",       "AREAS",
        "ASC",         "ASIN",         "ASINH",       "ATAN",      "ATAN2",
        "ATANH",       "AVEDEV",       "AVERAGE",     "AVERAGEA",  "BAHTTEXT",
        "BESSELI",     "BESSELJ",      "BESSELK",     "BESSELY",   "BETADIST",
        "BETAINV",     "BIN2DEC",      "BIN2HEX",     "BIN2OCT",   "BINOMDIST",
        "CEILING",     "Cell",         "CHAR",        "CHIDIST",   "CHIINV",
        "CHITEST",     "CHOOSE",       "CLEAN",       "Clean",     "CODE",
        "COLUMN",      "COLUMNS",      "COMBIN",      "COMPLEX",   "Concatenate",
        "CONFIDENCE",  "CONVERT",      "CORREL",      "COS",       "COSH",
        "COUNT",       "COUNTA",       "COUNTBLANK",  "COUNTIF",   "COUPDAYBS",
        "COUPDAYS",    "COUPDAYSNC",   "COUPNCD",     "COUPNUM",   "COUPPCD",
        "COVAR",       "CRITBINOM",    "CUMIPMT",     "CUMPRINC",  "DATE",
        "DATEVALUE",   "DAVERAGE",     "DAY",         "DAYS360",   "DB",
        "DCOUNT",      "DCOUNTA",      "DDB",         "DEC2BIN",   "DEC2HEX",
        "DEC2OCT",     "DEGREES",      "DELTA",       "DEVSQ",     "DGET",
        "DISC",        "DMAX",         "DMIN",        "DOLLAR",    "DOLLARDE",
        "DOLLARFR",    "DPRODUCT",     "DSTDEV",      "DSTDEVP",   "DSUM",
        "DURATION",    "DVAR",         "DVARP",       "EDATE",     "EFFECT",
        "EOMONTH",     "ERF",          "ERFC",        "ERROR",     "EUROCONVERT",
        "EVEN",        "EXACT",        "EXP",         "EXPONDIST", "FACT",
        "FACTDOUBLE",  "FALSE",        "FDIST",       "FIND",      "FINV",
        "FISHER",      "FISHERINV",    "FIXED",       "Create",    "FLOOR",
        "FORECAST",    "FREQUENCY",    "FTEST",       "FV",        "FVSCHEDULE",
        "GAMMADIST",   "GAMMAINV",     "GAMMALN",     "GCD",       "GEOMEAN",
        "GESTEP",      "GETPIVOTDATA", "GROWTH",      "HARMEAN",   "HEX2BIN",
        "HEX2DEC",     "HEX2OCT",      "HLOOKUP",     "HOUR",      "HYPERLINK",
        "HYPGEOMDIST", "IF",           "IMABS",       "IMAGINARY", "IMARGUMENT",
        "IMCONJUGATE", "IMCOS",        "IMDIV",       "IMEXP",     "IMLN",
        "IMLOG10",     "IMLOG2",       "IMPOWER",     "IMPRODUCT", "IMREAL",
        "IMSIN",       "IMSQRT",       "IMSUB",       "IMSUM",     "INDEX",
        "INDIRECT",    "INFO",         "INT",         "INTERCEPT", "INTRATE",
        "IPMT",        "IRR",          "ISBLANK",     "ISERR",     "ISERROR",
        "ISEVEN",      "ISLOGICAL",    "ISNA",        "ISNONTEXT", "ISNUMBER",
        "ISODD",       "ISPMT",        "ISREF",       "ISTEXT",    "JIS",
        "KURT",        "LARGE",        "LCM",         "LEFT",      "LEN",
        "LINEST",      "LN",           "LOG",         "LOG10",     "LOGEST",
        "LOGINV",      "LOGNORMDIST",  "LOOKUP",      "LOWER",     "MATCH",
        "MAX",         "MAXA",         "MDETERM",     "MDURATION", "MEDIAN",
        "MID",         "MIN",          "MINA",        "MINUTE",    "MINVERSE",
        "MIRR",        "MMULT",        "MOD",         "MODE",      "MONTH",
        "MROUND",      "MULTINOMIAL",  "N",           "NA",        "NEGBINOMDIST",
        "NETWORKDAYS", "NOMINAL",      "NORMDIST",    "NORMINV",   "NORMSDIST",
        "NORMSINV",    "NOT",          "NOW",         "NPER",      "NPV",
        "OCT2BIN",     "OCT2DEC",      "OCT2HEX",     "ODD",       "ODDFPRICE",
        "ODDFYIELD",   "ODDLPRICE",    "ODDLYIELD",   "OFFSET",    "OR",
        "PEARSON",     "PERCENTILE",   "Percentrank", "PERMUT",    "PHONETIC",
        "PI",          "PMT",          "POISSON",     "POWER",     "PPMT",
        "PRICE",       "PRICEDISC",    "PRICEMAT",    "PROB",      "PRODUCT",
        "PROPER",      "PV",           "QUARTILE",    "QUOTIENT",  "RADIANS",
        "RAND",        "Learn",        "RANDBETWEEN", "RANK",      "RATE",
        "RECEIVED",    "REPLACE",      "REPT",        "RIGHT",     "ROMAN",
        "ROUND",       "ROUNDDOWN",    "ROUNDUP",     "ROW",       "ROWS",
        "RSQ",         "RTD",          "SEARCH",      "SECOND",    "SERIESSUM",
        "SIGN",        "SIN",          "SINH",        "SKEW",      "SLN",
        "SLOPE",       "SMALL",        "SQL",         "SQRT",      "SQRTPI",
        "STANDARDIZE", "STDEV",        "STDEVA",      "STDEVP",    "STDEVPA",
        "STEYX",       "SUBSTITUTE",   "SUBTOTAL",    "SUM",       "SUMIF",
        "SUMPRODUCT",  "SUMSQ",        "SUMX2MY2",    "SUMX2PY2",  "SUMXMY2",
        "SYD",         "T",            "TAN",         "TANH",      "TBILLEQ",
        "TBILLPRICE",  "TBILLYIELD",   "TDIST",       "TEXT",      "TIME",
        "TIMEVALUE",   "TINV",         "TODAY",       "TRANSPOSE", "TREND",
        "TRIM",        "TRIMMEAN",     "TRUE",        "TRUNC",     "TTEST",
        "TYPE",        "UPPER",        "VALUE",       "VAR",       "VARA",
        "VARP",        "VARPA",        "VDB",         "VLOOKUP",   "WEEKDAY",
        "WEEKNUM",     "WEIBULL",      "WORKDAY",     "XIRR",      "XNPV",
        "YEAR",        "YEARFRAC",     "YIELD",       "YIELDDISC", "YIELDMAT",
        "ZTEST"
    };
	Functions()
	{
		int count = 0;
		functionMap = new HashMap<String,Integer>();
		for(String s : FUNCTION_VALUES )
		{
			functionMap.put(s,new Integer(count++));
		}
		
	}
}
