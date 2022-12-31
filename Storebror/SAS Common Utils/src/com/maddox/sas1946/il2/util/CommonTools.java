package com.maddox.sas1946.il2.util;

/**
 * "CommonTools" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to provide helper methods which don't fit into any other SAS Common Utils Category.
 * <p>
 * 
 * @version 1.1.2
 * @since 1.1.1
 * @author SAS~Storebror
 */
public class CommonTools {
    
	/**
	 * Override default Constructor to avoid instanciation
	 * @throws Exception
     * @since 1.1.1
	 */
	private CommonTools() throws Exception{
        throw new Exception("Class com.maddox.sas1946.il2.util.CommonTools cannot be instanciated!");
	}
	
	// <img src="doc-files/cvt.gif" width="400" alt="cvt Input (x) to Output (y)">
	
    /**
     * Converts an input value range to output value range lineary as depicted on the image below,
     * where the x axis shows the input value and the y axis the corresponding output value, with
     * each min and max values for input and output lasting from 0 to 1:<br>
     * <img width="400" alt="cvt Input (x) to Output (y)" src="data:image/gif;base64,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" >
     * @param inputValue
     *            the current input value (x axis) to be converted to output value (y axis)
     * @param inMin
     *            the minimum input value (x axis) for conversion.
     *            Any "inputValue" of or below "inMin" will cause the method to return "outMin".
     * @param inMax
     *            the maximum input value (x axis) for conversion.
     *            Any "inputValue" of or above "inMax" will cause the method to return "outMax".
     * @param outMin
     *            the minimum output value (y axis) for conversion, corresponding to the
     *            minimum input value (x axis) for conversion.
     * @param outMax
     *            the maximum output value (y axis) for conversion, corresponding to the
     *            maximum input value (x axis) for conversion.
     * @return the lineary converted "inputValue" ranging from inMin to inMax,
     *            mapped to outMin to outMax
     * @since 1.1.1
     */
    public final static float cvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        return doCvt(inputValue, inMin, inMax, outMin, outMax);
    }

    // <img src="doc-files/smoothCvt.gif" width="400" alt="smoothCvt Input (x) to Output (y)"><br>

    /**
	 * Converts an input value range to output value range "smoothly", meaning that upper and lower
	 * bound values will be reached progressively like shown in this image, where the x axis shows
	 * the input value and the y axis the corresponding output value, with each min and max values
	 * for input and output lasting from 0 to 1:<br>
	 * <img width="400" alt="smoothCvt Input (x) to Output (y)" src="data:image/gif;base64,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" ><br>
     * Note that the blue line shows the output of "smoothCvt", the black line is the corresponding
     * output of "{@link #cvt(float, float, float, float, float)}" for comparison.
     * This method essentially is an alias of the method "{@link #smoothCvt(float, float, float, float, float, int)}"
     * with the "smoothingFactor" parameter set to 1.
	 * @param inputValue
	 *            the current input value (x axis) to be converted to output value (y axis)
     * @param inMin
     *            the minimum input value (x axis) for conversion.
     *            Any "inputValue" of or below "inMin" will cause the method to return "outMin".
     * @param inMax
     *            the maximum input value (x axis) for conversion.
     *            Any "inputValue" of or above "inMax" will cause the method to return "outMax".
     * @param outMin
     *            the minimum output value (y axis) for conversion, corresponding to the
     *            minimum input value (x axis) for conversion.
     * @param outMax
     *            the maximum output value (y axis) for conversion, corresponding to the
     *            maximum input value (x axis) for conversion.
	 * @return the "smoothly" converted, progressive traverse of "inputValue" ranging from
	 *            inMin to inMax, mapped to outMin to outMax
     * @since 1.1.1
	 */
    public final static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        return doSmoothCvt(inputValue, inMin, inMax, outMin, outMax);
    }

    // <img src="doc-files/smoothCvt2.gif" width="400" alt="smoothCvt Input (x) to Output (y)"><br>

    /**
     * Converts an input value range to output value range "smoothly", meaning that upper and lower
     * bound values will be reached progressively like shown in this image, where the x axis shows
     * the input value and the y axis the corresponding output value, with each min and max values
     * for input and output lasting from 0 to 1:<br>
     * <img width="400" alt="smoothCvt Input (x) to Output (y)" src="data:image/gif;base64,R0lGODlhkAGQAbMIAAQC/Pz+BAT+BOyC7AT+/IxGFAQCBLy+vP///wAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAgALAAAAACQAZABAAT/0MhJq7046827/2AojmRpnmiqrifivnAMH3It03aO4HrN97Ef8CUc7oxB5ExJZA6e0FhhSi0sXYesdissIgjgsBh8ADMPgrR6zV57gbiAfE6vyw/yM2DP7wNiFExYgkeCbz2HOok5izaNPoSPN0hQlQMvVZlLXJxZSS5joWNlBEZqaG2pbJIvd3avdngBRnwHfrcwFZG7hrxnvkqsV7+9Q5aXmJlWPp1wX6Jikz2pqG2IQK+ysIxAtwC2fTkWwEjCTcXE6cHkRuaDxlE8yo6c5dDXNqppaOgysK55yunwtufRBXZD3BVSJ7BfQ4btEMqo9E4TvS1IyESLKEOfgHcQ///NWbhOBsE9IMUFkiARH8SELRXF5OaQYw+KRywy05IxDCl1+qSlEym05AuCRWvoQsCyJkynzqC6NEr1YdUaOHNSuYgRSCiFOFTR/PVvbMmTjnQsZWpgZlqpMuGatUrX5tWJUbBUYYSxUSiS5cTGVRJAG52oerxNBRToRdOXiO9GrvsU8mK7lGFkRaAzCM+UNcacUyKgWhrMPeYYzjwQXLjJjhvHdgtJ7lvLgyVfrqzbxea9Oz2BhvF3tKnSanrDqCMLt8mCfO6unW2jreNhym9nr+1cO+vc3xHkdQH8RtfhoERjn3bqY/dstBH4sVVzOnWludaj5h3e+37Y/P23m/9mT8BQnmfCGfeCep8AsUpMr6AHYAzegMXYBNVZh991CgoInodzgehfgCRO6Nt4nE3BVVLPhDFiDNZYWEOEHZbogmKA2WVfLhrK0GOPMnbH3XYs2vihkSESWCB5W5n32XoMFgmDNTmSaIeUA8rnh36V7cijOPnVaOKRYyZZ5otZkpkmigcS8SSULprpAhtcTnblkJTh0mBJXn4JJlt1pimnmoQOaiiahSLKpopOvvHDRnLSGehgNOKJGY57CtSnnyoxNemhliKJKKhYJhqqiVm1icWbw0R5qqSfvnUnqXvqeSoim3LaA5DxZSriraaWSmust6ba5AysXuGqpVT6ClP/HaLWZuuojsi2a49/iikotcAOq22wzp5Z7JJMFlDEedJACl6z4SICbbSTTMstIAdgaIQEnWgxQb789uvvvwAHLPDABBds8MEBQ6FFFQg37PDDEEcssb+PEdkuuMQKO+/F3kqY5XgMI5gIKXFGtsa2MQAkJIWu/fFrbAbUy0TF/XW7ccYc3/xtx1WiSu5WXSToiLounYyyC3TgYRuF36BkMQXu0PxyzjZXrbHVVF+tdbjGWvEDuj6U/BSspDLXax/0rVxB1NhOjfPbO+vsMcZxY10nTnt9nWwQYjtDNqjQBtnDfLTpwnavcM/Ns+A1b514zyAD7abQof0UzN+Hvsu4/w1bbu5jY4cv7Xjdoysud89HZ831z4zyAHYMGpVyeXIgvot6oXp6zqO9t2eIOOmqP6576sJLFPkyrlMug0ZMIHfad7b3jqitw7PFu/Sf/2663cAXL3rw3YuZF3A4vE4cGYI4b1n02GO5pfdKWdu+ro2Dv33p88Nvsf5Klov8qso7HwHMsQ/3SIZ9+auRvBIooR2FbmX2Y6AEq7c47VUJb4wqxN4W5BPSFNApCJzgc14TQS946YH7C9/9SmhBCp7OCxhchgYDyEH0IeEU7AihCGHwPu4Np08orB//3KbCHUJwiOJhnQxpYL4azs8N5NChEbUUnQruTmoqxKK48OdCH/9OUYhFhOGSysPEDbaoFJJ4UD+kaMQevvCKbUucFokXxhba8XtIHN+xypiIaKQxOUFi4w7d+EYX5IqBc6TbCpG4RRbisY54QcaxdtDEM27PaDIS5AQX6EUcHBKRcWwkI+m4SEge0ZT9S5EMKWnGjTRCUhbS5CZJSMojfBKUKpEaD+TXyTuespTA/OKJJJlBVh4CUq+k3QRlycCmOY2IWLglLuNnSGx50jqJFCa8HPlLbcImhsiioSUVZ7RgroaI0HEZGKN5PVJqsSk0u2Y1a9lFLvoyhcOhyIEqKbZEkM0dhRmJEGuhznXGrJ3uvEAW8LWvTuCrXjGbmEQnStGKSkz/YQdgmEU3ytGOetShocRQPGGWzXpyE5/epGcky4WgHKjrEOUMnytERzg87iuUinwnNq2ZPUWm1KcmHeWH9LhKVtqAaDBVZvdUQ5vO9Qpq2tPpPKspT0AB9Z7r/KkVUznJJiI1H0oFXtLc8r56ri2qOJWNWhFqz0eaE6vQzBFRw/mGvvUspqQzm1x62EXDoXVmMYPrNoW6VZQWYa6TO6pdvYDXugUuJm504VKCOtVuUvatbtVqIfu3TxoSrUqN3d5hUkpICq7lstncrGEFK0pU1iUKZNygXXMUWtON1puE/Ol0UIvTq2b2sprtZU1gW0zzfZa2Yf2U7TyX25Tah7es/1Xpb6Pr2xfos7iena0Qaju36G2uud504F+nS17LUrewSSxQV7Mru6Rw93a3xexRaHlSksYRuuVdbX6zClwhIBaAoZktSd6LuvjKl4rPFC4cVXuv3qI3rv09L4P/a1wB/4DAb0BgkMDr2k3h17z7hbCEFTxMVS5xbwJeCIF7ZmDMchiVufqwfkFM4xnbOL0mnlwRjguSFXuhxW99sSkPKWP+jritNTYyZ7GrPB4XYsU50iFYhAzJTxZZxCEerGuxzBDirvJ1KX5yci0FZHPS96q3vLKWD6zkJHO5Kl7W8Y7NwA0oA6bMwHRmQVvLTraSuMFHru+b23zjOAOQck4+gv+df8BGdzjTsrx8MBBS++dBW3rNwU2KoY0qhESbxip4LiV03HpQSm950hH9qKpXzepWa0FhVHC1rGdN63yZWtCYjnCW+UySSsSWiQJsryPGTBNNsgJtiLvprtXiYAZfmte6rrGv9+iJR4V5B8SWlUCPVlMIQnXZnQK3pKEdaP1tui8ChMPzBBJqYjlVdGcVNzXlXelclxuV5+aJtYVdA/UFg5mNKGvhAhFtJNyasM62N72RjGNql6+G/O4IKs7Q7k/x1S2TvbdV3axwjpM7v/l+eHroDFZ+UDwgIKKlaWVTcEAvHNcf97h0S+xwkFz7gyefxXfoW73davzgp475jZ//PfOGt+4zPLh5aQQRUJ2zprQtee7Pm11voRP66m82dFdwkGgcEqbpkMmt7sT78vsMveNnt3rWn0C+BHE9zF5Hwkxf0lzPnXDqGs80zItOaCi03XUj1wEU5X4HdoB3c0DEe9mDzneiVze9NR9E1+M+BKbW5MwdjjSboQn0zaMd659f+wAif4TJf4QVY3UI5mGsec/zufN6R3jVG492vx/d7S0SPCCNoFd1vDhIRFa8zB8PerXXnu23Bzzcd1/5kWx49VVuPeNzSnWGF5/2xo/5tJEnNBqkGJPND4iMqGyhNAv/c7okafaxz37i22T7ACaCk8GPDYFaiMoTlGbLhyDV/42/A54Otn+hN4Dap163NwPzt26NcFv3V0XQJn17R32+43+FAIDtN24XmHCIoXXd933KtIDbNmUO+HoQVX2ul1AWsFCllmqcADW19oIwSFG2NwUxWIM2yFE61RYjZVUllXcCuH6DMYNe8yYkkwNKlQjeJQh6tj/KNnwd0387aIHuR4AZOHtBiHzc14EkByNHqAMGZg7p5G374oOCAIUagi9T1YOLd4JTWICjd4AIGHEvEFOH0GJg2DTw9m1OKDdztFaPAYGxN30Y2IYuMYPxJ39yOCfr5jF2qAS1MHAY8oMJBViSSIiWOIhjYYhGtSCW0xFhlWHbhjPIZhsZt4bTtP+HVoiJGjhzmvgmA7SFU/KJNgBkktBtynFaZAhYuYiK1ud4mYiFh/gFZQBWi4geZVaL0dFz1lKJT2iCsteLVHiJm2WI6PKKxGhANXKMQ/AawyN1phhEvriK0piK0wiM58E8/VaMw4FnAceNEkF2vGg/sMeM4qiK5Mgd1Cg06MiF6hhlocgxF+cUd/eNZeiMgliPCHmPSJaP1vYGoSUEobYIKscOiUeQumiR8fiMEUgT8OeKA0SM3hGRAzGCX+Rhu2hwBsmG9giNQGhF0xaMNsSP2Igd7XYIUNcPMXaSLpeRB6mQG1lIrZggHeSJ/UgSNck5ZwZ8EEiPPjSPOhmOPun/PQyZdDEZizM5DBX3BpgnI1b2lKjmlS2ZkCyJD1NZQ0XAXTyQlTWweuUHiIGokvPGk3AZlT3ZVgwpQGeZbXHwj/5xeJGgf2DJbIE5jmNJmJ9wl2ZJlHFRcYDBYWDhlnNZmBMol29ZmUwpDYh5RtuVbUfAmI1JkvJYgmlXhYxxg6Z5mqs2g6i5mqxpME6JkaNpmFJpjkJpBpt5lUHgmQshZMLQhLG5kvQDlZIplj8pXKr5JC5yYZy5A7qJA+SHkmgImwaVkpY5mMA5m29oLshpm1aJmzPAl3EBfRkTb5SpZmFJl5FZnHaJfJtoScrpnd+JcjAhnsTiV9LJedR5meq5/58aeQW2155+ZJWp0RzBgH/SU4rlOV4Jep/R6JLsuZ2ywwMEBnYFCpqGgosM+nr5aZ3E2Z+Q9J8Q2mOcOXcVumdp4nMZOokpKpt1yZ/nAKK1GaFzWJStEFBM8Gi64Y0L+kuvuaO/2aEf+qDC4UqKSKMIYHlIEIaSAY8/ip492qTDiZ7VCXnaOaRxEhYjWniOiIcvMZA+Op0cKqX6WTzT1hUlg6XweaRIuo0FQZGaN6YtanbXx6Lp6aFvVaYx2gTvlXps2qYOYZIr2oxhGqWE6qKDcJxU2V40wF2HgYyPiJPSB6d1WllQaqhTmqCIOnKKigBZmgeO+gdK6WdxOqrA9P+kc3qdpFqdmTpO2JZto/UI7lgMXRmofLihtCqmZLiq/cSo9tcNsfoL5nerpdOHCHVNSzmolhphMPp2wlYNOcCAvvpMjwmZkvqWZugravilwpmsVGqmW+issxiCgzOC03pr1WqeUqggqXWuyEqmyIcRL0Vs8ZUIkVWGonmqQFqqCgVRJUgxLNiaAFuDthewBFuwLRhSLLGDlIqqk9qwqvquVspvExeuTucxSakEvomvuHqRPaWwFJivl1qp7vqGnyE2/uYP/6iVF9tg0amtDRqXjFFZLWtq7CqsLDSwETslE4uy8lkldceyLSuyqQqO6ldNf1h9Neuy5Iaz3meyJsf/sxWLHmzJfwTXrqQKdEGrsYVqpw9Lsp4AKTgHtSwCfY2AoELrsER7nls7tLkKsU0rbGG7HOApBI55LbyTtGdrqtvKtXgrEEybezMKij0LGuKZCCiqtKQZnC+7sYh7VTgbeIE7I+BJEvR5CDp6tiG7k5jbt3tbR48LuJy6D5I7uCDBmxlSrFbbsHq7uGuLtnbkdzwBtqLLs2NBn1XipZt7flrLrTY7slXKqpSHNJNbCLabIxXZuAy7umrLu8jrCL4WuyQ3eHJLuoOAf0IAqM3rpLaava2bue3wvF8bvcErksVLEjnZu1e7vbmLvmj7tyXDfDVKvVhQvgsRfOxrreq7/7t8m7o7wLSye3rTG7U3QL/Guq78a56Ju7/3Cxb+Gycnw2jDO78J5gzByr2GqrwJzLYL/LjvC7/thqPlAJgLrKDra8EKfAYcLL7ukZYRrKQhLKrdy7lfOcI0bFkp3F60w8ICzDRpY3D32rmuS2r/arBEXMRMW8RIvJoYTKfey7olhLMbQX94NoosKzMmrMEYm79A3MTLKzxQ7MAKqKbyi2CsoIclfMaaq79YjMYQBrtZAMZhPMXJCLSBVcN5q8VOfMJsnBBuXIShq4ByPMeoFokHrLtbLMNdHEZ9DMfxKb+/mktNgcgZrK+FbMf85cZRvIjakAMktAgYesVBnMWVDP/KlonJLkKHkxuQp1sxkszEaTvJXAzL/WXKsoPK1DuRq8xTlgzES8ywsezKkUDLRbocBDpCe/YGTKrGoYxPvQyyrezLByDMMUWhTEOSJhRpz+zMBTnKe5zIWCDNykSi1WyiRXC83SzL27zLebzG5QDO2CjO8zXBoIG95wzMHFvP0HzA7iy3XvCz9mXPpPzKAI3P2iwI4GsG5bSm8WyiIGG/AW3I67zMylyZz3vK4aylPGSh/weI2cy4KMnNE12tA2vRBsSnN0K21kNpHR3D6Rw/6We0zbbSzItSI43Q62bSCKYIIvzQa3itV5CuBc3T3UTLRtOoGS3PQQCZv5zPQjz/gVFogjKtx1lF1LRj1Cet0Yb0wxG91PmKASrYryDVUEk81hbVx2R91jWYgxRYVdlK0B491Wz3xqVQ1b2a07lkxW7N0k09b0+9sHot1DQd16RQ1HUNfWYc0iBNtU59hnUM1G99yHlnyicDrYZ92JCtztcCyUWb0t7M1Y8ND6NHMnSNci3D0CndFlHNzvhJiYm91cAFu2Ew2jOwemaL2JgtmLc90BMN2wQw2aHoGrms2q4N0Z2d2hJtBLw92k63hOjHyq1d3C2d1zNt204Q12CQHPEFwgsm1Zctl8382dBtisl9Grc1as3dNsbt2cOKx+EN2HAd2tdN3ihHUOcN3ujc/9Psfd/uHVfjLcbL7afb/dfSbaffLeDUjbkY1dvyHRBUDNM4ld7GXeDT3d0I/gSDnQajZYvnu9/Dfc8H/uEUTgkWHt8CkOGCfNr6PeDp+9wpvtsjruAm7jQFHNMsrtuKndtMLZeVcOGHIXAVrOLHvWYSzt0druMKE98C1ck7DeTqLY/5beNFfrawrQYjoXJKDeGpO+TC3d5Sbt0YXuUOuIJMjuWUiNZmboNmfeZq7mpaHuRcrsxTXuJy8D4ZG+J2HtFt3uRE7s1xngevYdlRDuJ4/uQ5Puba43fXPQd/DuhvfufenOdkDsqI/uUBEA72yeGNLuSEHtSG7laTLueWDv86NV7oYIrjnA7nbAfjlY4Sn9zpmI7brw7lfD7ioM4Hh+vquJ7Gjt7iu54DR17rAHC5gp7pj77p9i3rk/zrcx7svBTpuX7jsU7qQKwwwI7No37qq23qx86w1L7sb3rt216r4G7g0/4Evb3qzP7g4z7hmr7ue57s5l7iewDDzj7sKhrt2B7RUEAAcw7Der7lvD7D+B7u6Lnvq27A2k7u7Z7w7D7rAyDvKu3uAC+BqywN44DsGM/tTyAAzN7r0h7o0B6za+3XH0/s8D4AlQ7VEu/m9y7yT+3v/87ydAoFAeCM9e7xtZqC/CrWXPBQD7XmQJ8wGBX0RP9Rau2xJP/uGd//oQawGThP8Aqf2Xyty4qr9CWfuAYQAChi70sP9VWfrtHp2FEf8GprAACg9eTy7CA/6JoN05t99V0fpRIAAAKw9U8/9nFf9Spx82t/JnMfAHWf9lwP94QPs4Of7yZfTXsQAARg931P9pAfbgOP94M4AWfP+I6f+IXv9XJ69w0P+ZZ/9gLQ+ILv+VaP+LCu9pqvgaEv+qSPDJP/+XQK6SvfxBSw+IAfzaX/+Hl/+j0V+77fi7eP+6WR+ZG/+ZRv+KY/8aReAcQ/QMbf+8yP+pKv+sevWs7//LoP+9Yv/apr7MnP+UM2/Nof/cgv++ff+byf/hqsC3wgB2lAAAVg/tTP//4yP5ndb/8i5P7vDwFBCFLGRVlvvk8Huy8kkbEET5RTV611zZidvTozjDuD3X79oYKlIakYOqZ2plwTAYACAtNJ5TJYJkVZ7k5L83ZvX01OXCO/zrM0bx1rM53P6FRCsGLC+/Hb5wcCFNox40MTJEI0UkQiPJiLAkCgojiw0OszZGNU0oTj3LrJOSAtNT1FTVVdZW11fYWNlZ2lrUW9HLDV3eXt9f0FZi3MJD70/DsOTA5petSInKQksLwCBSveXE7UXuQGGTbG/vTuFEc2Z25+joqWpsYMj89GHyQPpd+uAZ+XH8fv/tfIXpNR66C0m1AJlzUb9q71OwdRWcAO+//8SaxH8R7GfBzLEJRB5yBCCtOuYHHYUONDfi0vuowYw2LMlzVp3pzoUWAJgjpkRJJkR0LJAycZqkmJdKVKneWacpiZE6ZUm1RxZvTYUw20dkPxFL0QR+xRN0nLLlX6dENUrFPbVn17taNbBFpfcBWa8OuVsWZDoj2rlildgDPYzoWLWG5hwjsx2r3Lrqtek2HJ/hWctrHTzRs7r/UJGPPnwYkZm3YMeo4HyUIR6CXF93Jf0bQzByaNI/Tt0ag5+/YMnKXu1c8OjOz6migu27lxCy+9ODV0zS4On5b+O3vw7cNpQl53XFIGO8qXy/bbnPrz7tGtvue523nv9tXrs4f/Pxd8+PHJKb/LZTYB0xsQsOumu4++/LBbEMGc9ssgChVc+4+5AnlTL8EMG6xIvvUUjItD7kT0biIIRQLgBNfM+8pCAl+s7ULnDtSORPdCxFExG5E6EUUVy0voPMtgxFDGDzeMz0gNldzRjR6h+TGADCibxsUYiZwPSR1zTIcEkIa7TksGuXSQzBSehJK88lhsEb0r3ywSy/WiKmSfFoqr0cwR9SyRTw++BIGrESj8D0Axy9wy0TEVRXRRR7/xUDdJmQK0UUvzZBTTRyt6hC2uZFgxyK8MZdJP+5oEMdM9VVWtJ1JGaUYVWA0IplZbb8V1FuZy5bVXX3/lhU4z7Jw0/8xSWb0R2WYiNag/UKfQICEERrUyTjizPHZTqJitU74RemL2VFPxGzdVbetShwSgPAiVSlKqxVbOJeVtUthJ0YXBWHrLPZS4gtSV7IV2q6DWTWsPjvfaOcMFtNJ7L1313GQlFrdVHYL4VGBop5xg2hbhPTJbiPtEtmKK0RWFVpE1HXliS+36YV0W1jTP4ypBnlfhnBFeeOWIWzYZaHL9DS3mgDUI4ICNzRPAZgAD3LfkoU82V+iqWe7QZ5Kp7ndV8HrIGGmlpeS444INTpjnndOeMVypr/4Z67jn7nM/GGTmIGmyy3YaQLi3trprwOUenG6Xs46aa63dg/Duo5HWe/8DafGYxm/BDzc8aMIxL7xzzj9aXPPMpw4cUjxxCxtypaOd/GwsLhfd89g/n7120jd/mO2QE5+uRxXw3sAOFYLseyHYbx/9b9qRl5355WlM/njlbZ8etx4xS13NpCXvuPhqpAc/dOepbwP65qtHP3xDrv8r+65OIN57DNTXGVX6bzN/+fTF35/oeoAPnvBYZzbKvet7/Luf7tZmHbcpjne4G19Z2FcW97XjL9KyWeWMh0AOPnB0+SNfB2M0QTdU0DXw614BDTg/EarNfuIDYQT7Vz9+kZAHAAwgtEYQPxVaroUKfKEHQfPD3dXGYUQw4Zp2iMEeblCI+ktgEfHXwNL/PTGEougUFVmDHBCsaIlNy+C7nEjDt0VxgfcZBbDUuEY28qoAu2pjHOU4x17EcIYu5Jc+YHWDFHGxAyuSAQ/xYAJLjBGPZSTiGVFgRzMGcSUESQMUjoMCmgWyezYj5BsPaEUZNjKPZmFkIh25SEjWQELO+mMlP4BBTIpxk2R0ICyraCAtRk+UZtIKGSRESVXGr5WFfOUhYynMWU7xloj0BsxmEIkhAJIHghwVMFnIyTsC8ZO0POYwCQMZLQClmZU0ASspVzlpvi6bxbQmMvV4TghWc1vF6SY7guDMZ16ScoQspzsVeU1itrN8tTyfJ9XZITwlQVAkoGc47TnIUuDC/5zUFKg2pSgTgEKRnSNq3Apk9oOEKhSMv3yaPkeZTonu00sVvaIs/Wm6mRzhUxwFZz01ME58LkSk/CQpOifKwIsG9A2+Q0HGYLo3yR1goQxdIUoiqlOTlrReKO1kTwdzvSEArwcdFYBRP3rPTNp0qSuFKAyhetOBpo19VX3cVWP6GqNugKuu1MNXbRlWToaSrm+aIMYe15uOsvWjINVkXKWa0n7OdZ13LSwYSGi0vcIAq1n96z0b6tXBRhWxbassWRdhQ8aiEjd9nUBbM0DTyVZDs0wd6U6bCqmTWuQERzRsYv8EW+24rwWgzapbJQtXpWZWrj4FJbd8QiyUFdepOP+FA21/g8PA4Da3Mx1kTSl7WdWmdrWIQxxxh1jW6vIki/JgbnPXah4VvJW3llVpbHMKVj2C61W0ilUqIElH+taXF4HNhX31u1/9CksH2gUdd6/7EfgaooJnce5zM8iD856WvbIl7Hq31SXjPkdf6e1cKfkQXqUk+ASkLa1gqTtg4GI4wNkNzb+6hdwBu0oMBy5LX2v24eiaAr/ohTCOJVxiwzBsNQ07nXq7a7H+wTjG450xdAnA4A/c2MFCJrFF64rSf5k4wiz1yVg4bAMZE4/GS5budK2s4yEfV2p2HbNmuckFI7uhy9IaAU3DbNrfSjnNdc5dmVGbo69loc08kHH/zUIi58mKOcdP5vGOnzdWPJOZwABmw59N8ObuxTm6cxbxnX0r1k0nI6N8FI9niYBkppWFq9I1CqLtfOhGVw/NrPbDidrQR1GPemkgYKWlwWxjhzraur9mMbBP2mlxANWUfbyB3ojaAV9+gNAhRomqrxzlaQubwprGElWPjewaKLsEzV4wPhHgZGn7OtjnFjApiR2Ps247DVNY3bcvKYNTl2LcNi13vlvd6FcrOqV5XeYuux05EviS3pe2N7n3vW5/V7jh+vafnrw58O3Je97ORjgp7m1aiI/Y2nsetsfzyFkXrIsMArQ4C+QsboUzXM8PxqzIy2jDqx00BkIpAit5/7DyhON74TI388n6/fLoKfdnL50Bzi3+VxPUu9Ac/zm2pd5jl68AvvkrAt60cMKlqzzjYvR51T8O84Xx1+xnv9Ub4Yh2trf9VkOntvOWpY2wJcGLBdf5zr9+77ADHeRB/ztr/U44DdMjdXZXIt7nrXcws7zlg4fy2COfJMgXzsXJwOERABmEvOvdAxpXO9TFjm7AQxDukh9cLv3AXM3T7AcGf8GlHd/3qT886lSvvMuUeQzb8vLWr+980xtvCr6LPvfVJn3gsQtrbqzZExUsgjOBz/TRHuDrTX587Yme6O2fmPn46LMmOPzNW9MH9tXfe/ExcXvbV/b0yc+T3c6wZf/yL7sH55+W9T8/AvxGm/3dR750ozztw4lPKwYYm6eYur/Fmyn9ewHQ678ALL3JE8AKvLbvgwhZe7G92pOEggH8y7/Gmz2O6zgCRL1VoyiXM7ZM2DL86KgPDL6vEMGew7cSxMD2Cy6G07YwaEEXXCsYpL7Rmobho0ES/L+4s8GYM0GsaDce5EDt6KsWAMGv2D/sq8EjPEEJVL4JO76zADgvkDS1Kj/NmMJp2L8MCD14wEL4o8AJ3K4l3IbF8rMn/A0ZG57gc5ozVL9oS0IANDcLFJr3A0QlIDlQo8M6RLI7ZEAhNMMH5D9y60MkXMMz8zHXerQs5AGamzXxwIZAU0T/EiggPUxDPpxEN0RBP7QXh/uW4Wog8DE62ZGkWpsLTxygIGREFbC3PTxFSRw9SqQw4lqxQYSK7yKNUzKEQLsgPBynEyA+XdRCsuPFLgSXNJoVWmmFNHK7bGQjtVs7bfTGb5QF/1JF1TjBudOIieMDZAwJEGwlR0TDUdxFTIxEefynX/QW7xNGdKmyp0iTPVDHdcTDdmSydywABDDCXjTFZ/wg4boXFQOorrk8irC5TPjHLwJFyXJHvjNINUTILeQ+NKrEFJsDcIE/1fOGjeKCf7SkRWTEJWNGjdtDUuxIaKRHQqAyleGd3fsHrUtJUrMBdnyrjBxFmezCeWTD5hHE/6BzPnywqixQSY+yRUYsi2aEx3g8SqvMR6y8QBxMB3gSBAASi6fUKpaUypCgSkg0yqxUyI98qluSv2U4PKccQyIIyHAbgVx0xrXUSo/cS5ocwBt8NA/ZkOxpA29Dg7oktIEkSBJMy4T8Q8d0NUZTEg3kPcLcAcNkA8REOMXkRsYsRb7Uy9B0OD/sjRXsh/B6N4KbAciKynAzS5jsTHhoTNB8TNqMzB/aQWwYP4qLt9XUqhLgOXx6xIL0zJlsQ9usR8RqwgM8sJODtx2ALBQ4NRk4S+KUzc/0S7WszeXjyi9kTj+iG6WrgdBqTUxSA7xUu43kw9nMTsjkN8l8ETncsP9DZB7xXM3ovEjZ+wD0fCP1ZMurFE32XEgOKkQWNLKtKw8ySIgiWDnqhEmNLM6ixM7j5E7SzCLo0bI2Q7wEHc/Qkk7ZE84TSM8IhUP3dD/4NKIgC5wevJ27u08FC4HpDFENGNHrNM7/1M7brCtiPJIwXIHNe1EY7YAGFU4aHUocNVEJJQtsBMcmZbvYDAsnldIpPYWkbBlzzIxYlMVGkb4Y8DLgBNEiJUj/RFLkvNFF05rCy1Ju4wh60oIvjVEZFVMIlU0BLdP2NNOU8ZmIFAyBowehsD8XiJ8gINI5rdH1tNO+pNBFRUoUbQqTXAlmEgQPFFQMItQwNdT+ZMxEDdD/CW1UkdFJfpTUZHjBFQC3OBXBjBzTTfVURb1TRsXHMlvKpjA5PwBUljnVIZVTcTNS60TUVu3UEz2W8AsIlDyGKEQBg/uBQuVVvvNVWA1WJc3BfXnLnUQ6T7DDZNW5ZcXUZnRWMnXVcI3WEhW8+jFAjmhKTbhVRIG9HmBWb01PcB1XwIxGbJIXyjS8ulsDWlQ8pnPXXcVLjZTX7YRWgn3Vg43VBTLNqci8M0BGzuu8f01VVaXTOgVWgxVXjM2ztclNiGA9MVjXMTk/GGDWZq3YXz3TeW04K3WK5TzN3vNHdZy+ICRZgA3YQ8XTnC1YTo0glr0H7wQv6OuCigwB/KvZ/4llMhHF2Z29WJ7V0SuRz+8ET4tSyQVkyRYo2ZtdWoRVWQvlNAIp0HBowbCsWlzDQ6zdVZON10112qZ9z9YSzDckAk3sgh4k27mcDnZEW6SdU4FlW7dN2Z5lyHEkDh94RYti0cIcm2V7C73VLUzt27Wt07YN3HJLRWAcTU5R0RX9M8XFW8ZgR8zIWq0tyIHV2NOl3Clzr07JImGwRiqF3VpQu9iI0ti1XXAURwA7DCx1DlrbUsMRHsbNidD9i9Hlz/403dQlV539VMHT3ZCEHS1dCfsMhyApzy8zAng9VERVXnqtST11XpFUAf/ax2KMRXsAVNog3kFLW3FT2tJNNf+uRV3AnVaWukR9xBNIVYtRpYhb7Yv1ZV/Idd9ezQC27V6u9N6/ZKAjCNXNAIoUYYR1HQvr/YOSLdL3LeDJPWCvlUZHxTItfGDNCtkJrksbsGDiw+Dk3eB6TeCQA79KEROe1IaQTR8AZjy+VczhhF8Npt/lLdj3q1Z0FapJddMssGHhE+Bm1OEM5t4V/l4fltupOFe3AEtAoOFOKmGvw2ExHU4NMOAebmHHhDt8lYiGPdZSvQHWHIP2bdYuVs8mBmMEXlkPxl9mUY8QFrIrrh3yLE8RYGO8dOP4lV8nBtDT7beObQk8Vi893mP8nIEh3GJvtcIdbmJCztGLfTWX5Qf/+nNYfvVSD70BSCYCQP5WFbbkJIXihDWRV+wLRZ4lRuacIEkDGRSCB5WBeDXlOObgMbOrqD0EV64iWMYc6yWDAorkgLVCL9bgU87TVN7YQAjbSOvcnvTJ26HgYqbCUbblQkLeXK5cXbY6RqNbQ6RPKBJmwKHgv1lGbW4BbqbkSgbnJ84xRjrc2tHS320eZfvciElndQ7F7LVlE8BlQR7keC7kpzUMHm0PY0QLeKvmiVHWGCAtbRaBgV5mZmbejLXc173djk672aXd/PLokXa7/OFd33jgrklfQejn20lMJAhYgixdU8ZophVWiipfgtqNfAkNPC4CGH6n3QDUioPb/6lqgpElpRwYPpgO6DFVZpQA6sAMl5/2ylZRlKgmmqlO6p0O6quu6vsNxFriUxTTDEiQYXtMi2CUYMobhvE1gAnIgUUcAqUmgLb+Bm/t1Zk2imDEDBqZa1ZMa8BGFL7+Abb463tx62dOFcLeLsO2SQZygsM2rvFFF2CWbLsOsBwY6ophbO4RAMc+KTzA7LXA67x+aizo7IgbbMHuay267KIpV0xJ7eICbbQODL4e0K3OsiRZDZ424y7p7XSg4cMO7mgxur+u69HWjdJ2aiaWlOIuFq0eNuiu40QZFuVW5aG5bti26kXZ7sBW7DtZi9j+YO2+bs0AgBx4wr/+L+5WDf9Kba3JrsXPlm4QSO72DmzmLmXn1kf5Ju9kcQLlxm2SCXD3zm7zxu/bZm2vOW/rUS7xLoP/JjLOZu0TiAL1tu4K3xZkDTm7tt6mqW0OEG0DWDIBPwUQ4MbTju5TCXGyVvDwZmsDV2zzRuzGru/wZfEbx4Edl3DVpnHjUu9IuDAcn5QA0OxEjPGPSIgBjw7K4WtwMIVIweU3XvGGXHACZ+1hYHIKt/Iolu0sF0kY7/Awr3LtYAvyjdv40PImgAKSbBQgO3I31xQ4f2sQ39zeGKf7FslCiHIPSfGn7m78Bes5/7FCPwyqXvM1XxA6H/SIYXR/QYzh2koft+f0NoByth3/IzeAc27kuO5jpshzHOZz/d5vmp5xq4mhOEj1JVj1x8aG3YgUi1hYgDklfPac4H1o5pHlT7+GUN+GE0fx2VXxgzboS4aK8eZORA6UlOaEoR6bgPhwIR2HPFcGUufGAjgBgs7ormXhOZ7wMnlgeUKEoUY5fIj2phELXwcCYA/2/hwBbd/2+f1mV69uxQh3Z6HjrtjsuvCDc/8oqIJkNq7CppbpsoD3iriMfLe6hGf4aY3aewdP+CT3K0ZRf6e+ijJmARYBUnfW0v2Ag0d4v1D4RWp40Rj56a7nUAt3L0k2fZ54ZjBii4f5D81zB/QBdu+Aa1cDkD92kS953jh5lg8ugPZhpnsneYSa+MXldCqK9rFs6W+w71Cv+ZtHhRC49pk2AZ7veZP/+fkI+pmnJYeBeN+19UnY9IlHe33WB4v3d6eXaBKX+rjftY2/Rg64eos+iUoOea73+b7n+7CfxsAX/MEn/MI3/MNH/MRX/MVn/MZ3/MeH/MiX/Mmn/Mo3/AgAADs=" ><br>
     * The output of "smoothCvt" with the factors 0, 1, 2, 3, 4, 5, 6 and 7 is depicted by the
     * black, blue, yellow, green, cyan, grey, brown and violet lines respectively.
     * @param inputValue
     *            the current input value (x axis) to be converted to output value (y axis)
     * @param inMin
     *            the minimum input value (x axis) for conversion.
     *            Any "inputValue" of or below "inMin" will cause the method to return "outMin".
     * @param inMax
     *            the maximum input value (x axis) for conversion.
     *            Any "inputValue" of or above "inMax" will cause the method to return "outMax".
     * @param outMin
     *            the minimum output value (y axis) for conversion, corresponding to the
     *            minimum input value (x axis) for conversion.
     * @param outMax
     *            the maximum output value (y axis) for conversion, corresponding to the
     *            maximum input value (x axis) for conversion.
     * @param smoothingFactor
     *            the factor setting how many times the smoothing gets applied.
     *            This factor must not be negative.
     *            A factor of "0" corresponds to the "{@link #cvt(float, float, float, float, float)}" method.
     *            A factor of "1" corresponds to the "{@link #smoothCvt(float, float, float, float, float)}" method.
     * @return the "smoothly" converted, progressive traverse of "inputValue" ranging from
     *            inMin to inMax, mapped to outMin to outMax
     * @since 1.1.1
     */
    public final static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax, int smoothingFactor) {
        return doSmoothCvt(inputValue, inMin, inMax, outMin, outMax, smoothingFactor);
    }
    
    /**
     * Calculates the string distance between source and target strings using
     * the Damerau-Levenshtein algorithm. The distance is case-sensitive.
     *
     * @param source The source String.
     * @param target The target String.
     * @return The distance between source and target strings.
     *            The minimum return value is 0 (means: source matches target string).
     *            The maximum return value is the maximum length of source and target strings.
     * @throws IllegalArgumentException If either source or target is null.
     * @since 1.1.1
     */
    public static int calculateDistance(String source, String target) {
        return doCalculateDistance(source, target);
    }
    
    /**
     * Calculates the string distance between source and target strings using
     * the Damerau-Levenshtein algorithm. The distance is case-insensitive.
     *
     * @param source The source String.
     * @param target The target String.
     * @return The distance between source and target strings.
     *            The minimum return value is 0 (means: source matches target string).
     *            The maximum return value is the maximum length of source and target strings.
     * @throws IllegalArgumentException If either source or target is null.
     * @since 1.1.1
     */
    public static int calculateDistanceIgnoreCase(String source, String target) {
        return doCalculateDistance(source.toLowerCase(), target.toLowerCase());
    }

    /**
     * Compares two float values for equality with a given precision.
     * The reason why this is necessary is that comparing float values with the equals operator
     * ("==") is very dangerous. For instance adding 0.1F 10 times to each other will <b><i>not</i></b>
     * result in 1.0F as expected.
     * This method is the same like the "{@link #equals(float, float, int)}" method with a precision of 7.
     * @param f1 First float to compare.
     * @param f2 Second float to compare.
     * @return Whether or not both floats are equal within the given precision.
     * @since 1.1.2
     */
    public final static boolean equals(float f1, float f2) {
        return doEquals(f1, f2, 7);
    }
    
    /**
     * Compares two float values for equality with a given precision.
     * The reason why this is necessary is that comparing float values with the equals operator
     * ("==") is very dangerous. For instance adding 0.1F 10 times to each other will <b><i>not</i></b>
     * result in 1.0F as expected.
     * @param f1 First float to compare.
     * @param f2 Second float to compare.
     * @param precision Precision used for comparison. If both floats fall within 10<sup>-precision</sup>
     *            of each other, they are considered equal.
     * @return Whether or not both floats are equal within the given precision.
     * @since 1.1.2
     */
    public final static boolean equals(float f1, float f2, int precision) {
        return doEquals(f1, f2, precision);
    }
    
    /**
     * Compares two double values for equality with a given precision.
     * The reason why this is necessary is that comparing double values with the equals operator
     * ("==") is very dangerous. For instance adding 0.1D 10 times to each other will <b><i>not</i></b>
     * result in 1.0D as expected.
     * This method is the same like the "{@link #equals(double, double, int)}" method with a precision of 7.
     * @param d1 First double to compare.
     * @param d2 Second double to compare.
     * @return Whether or not both doubles are equal within the given precision.
     * @since 1.1.2
     */
    public final static boolean equals(double d1, double d2) {
        return doEquals(d1, d2, 7);
    }

    /**
     * Compares two double values for equality with a given precision.
     * The reason why this is necessary is that comparing double values with the equals operator
     * ("==") is very dangerous. For instance adding 0.1D 10 times to each other will <b><i>not</i></b>
     * result in 1.0D as expected.
     * @param d1 First double to compare.
     * @param d2 Second double to compare.
     * @param precision Precision used for comparison. If both doubles fall within 10<sup>-precision</sup>
     *            of each other, they are considered equal.
     * @return Whether or not both doubles are equal within the given precision.
     * @since 1.1.2
     */
    public final static boolean equals(double d1, double d2, int precision) {
        return doEquals(d1, d2, precision);
    }

    /**
     * Gets the current offset between local time and UTC time, in minutes.
     * @return The time offset in minutes, as integer value.
     * @since 1.1.2
     */
    public final static int getTimeZoneBiasMinutes() {
        return doGetTimeZoneBiasMinutes();
    }

    /**
     * Gets the current offset between local time and UTC time, in hours.
     * @return The time offset in hours, as float value.
     * @since 1.1.2
     */
    public final static float getTimeZoneBiasHours() {
        return doGetTimeZoneBiasHours();
    }

    // *****************************************************************************************************************************************************************************************************
    // Private implementation section.
    // Do whatever you like here but keep it private to this class.
    // *****************************************************************************************************************************************************************************************************

    private static int doCalculateDistance(String source, String target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("calculateDistance - Parameter must not be null");
        }
        int sourceLength = source.length();
        int targetLength = target.length();
        if (sourceLength == 0) return targetLength;
        if (targetLength == 0) return sourceLength;
        int[][] dist = new int[sourceLength + 1][targetLength + 1];
        for (int i = 0; i < sourceLength + 1; i++) {
            dist[i][0] = i;
        }
        for (int j = 0; j < targetLength + 1; j++) {
            dist[0][j] = j;
        }
        for (int i = 1; i < sourceLength + 1; i++) {
            for (int j = 1; j < targetLength + 1; j++) {
                int cost = source.charAt(i - 1) == target.charAt(j - 1) ? 0 : 1;
                dist[i][j] = Math.min(Math.min(dist[i - 1][j] + 1, dist[i][j - 1] + 1), dist[i - 1][j - 1] + cost);
                if (i > 1 &&
                        j > 1 &&
                        source.charAt(i - 1) == target.charAt(j - 2) &&
                        source.charAt(i - 2) == target.charAt(j - 1)) {
                    dist[i][j] = Math.min(dist[i][j], dist[i - 2][j - 2] + cost);
                }
            }
        }
        return dist[sourceLength][targetLength];
    }
    
    private final static float doCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + (outMax - outMin) * (inputValue - inMin) / (inMax - inMin);
    }

    private final static float doSmoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + (outMax - outMin) * (-0.5F * (float) Math.cos((inputValue - inMin) / (inMax - inMin) * Math.PI) + 0.5F);
    }
    
    private final static float doSmoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax, int smoothingFactor) {
        if (smoothingFactor < 0) {
            throw new IllegalArgumentException("doSmoothCvt2 - smootingFactor must not be negative!");
        }
        if (smoothingFactor == 0) return doCvt(inputValue, inMin, inMax, outMin, outMax);
        while (--smoothingFactor > 0) inputValue = doSmoothCvt(inputValue, inMin, inMax, inMin, inMax);
        return smoothCvt(inputValue, inMin, inMax, outMin, outMax);
    }
    
    private final static boolean doEquals(float f1, float f2, int precision) {
        return Math.abs(f1 - f2) <= (float)Math.pow(10, -precision);
    }
    
    private final static boolean doEquals(double d1, double d2, int precision) {
        return Math.abs(d1 - d2) <= Math.pow(10, -precision);
    }
    
    private final static int doGetTimeZoneBiasMinutes() {
        if (!loadNative()) return -1;
        return jniGetTimeZoneBias();
    }
    
    private final static float doGetTimeZoneBiasHours() {
        if (!loadNative()) return -1;
        return (float)jniGetTimeZoneBias() / 60.0F;
    }
    
    // *****************************************************************************************************************************************************************************************************
    // Native Methods implementation section.
    // Do whatever you like here but keep it private to this class.
    // *****************************************************************************************************************************************************************************************************

    private final static boolean loadNative() {
        if (nativeLoaded) return true;
        if (initAttemptDone) return false;
        initAttemptDone = true;
        try {
            System.loadLibrary("SAS Common Utils");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        nativeLoaded = true;
        return true;
    }

    private static boolean nativeLoaded = false;
    private static boolean initAttemptDone = false;
    private static native int jniGetTimeZoneBias(); 
}
