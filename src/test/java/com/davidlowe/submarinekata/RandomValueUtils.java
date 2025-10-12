package com.davidlowe.submarinekata;

import org.instancio.Instancio;

public abstract class RandomValueUtils
{
    public static double randomPositiveSinglePrecisionDouble()
    {
        return Math.round(Instancio.gen().doubles().get() * 10.0) / 10.0;
    }


    public static double randomNegativeSinglePrecisionDouble()
    {
        return Math.round(Instancio.gen().doubles().max(-0.1).get() * 10.0) / 10.0;
    }
}
