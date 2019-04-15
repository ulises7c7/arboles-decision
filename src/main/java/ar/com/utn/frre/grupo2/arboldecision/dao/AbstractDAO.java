/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.DelimitedParserBuilder;
import org.beanio.builder.FixedLengthParserBuilder;
import org.beanio.builder.StreamBuilder;

/**
 *
 * @author Ulises Anich
 * @param <T>
 */
public abstract class AbstractDAO<T> {

    private final Class<T> dtoClass;
    private final String recordName;

    public AbstractDAO(Class<T> dtoClass, String recordName) {
        this.dtoClass = dtoClass;
        this.recordName = recordName;

    }

    public List<T> obtenerDatos(File archivo) {
        StreamFactory factory = StreamFactory.newInstance();
        StreamBuilder builder = new StreamBuilder(recordName)
                .format("fixedlength")
                .addRecord(dtoClass);
        factory.define(builder);

        List<T> dtoList = new ArrayList<>();

        BeanReader in = factory.createReader(recordName, archivo);
        Object registro;
        while ((registro = in.read()) != null) {
            T dto = (T) registro;
            dtoList.add(dto);
        }
        in.close();
        return dtoList;

    }

    public List<T> obtenerDatosCSV(File archivo) {
        StreamFactory factory = StreamFactory.newInstance();
        StreamBuilder builder = new StreamBuilder(recordName)
                .format("delimited")
                .parser(new DelimitedParserBuilder('\t'))
                .addRecord(dtoClass);
        factory.define(builder);

        List<T> dtoList = new ArrayList<>();

        BeanReader in = factory.createReader(recordName, archivo);
        Object registro;
        while ((registro = in.read()) != null) {
            T dto = (T) registro;
            dtoList.add(dto);
        }
        in.close();
        return dtoList;

    }

    public void exportarDatos(List<T> datosDeudores, File archivo) throws UnsupportedEncodingException,
            FileNotFoundException {

        StreamFactory factory = StreamFactory.newInstance();
        StreamBuilder builder = new StreamBuilder(recordName)
                .format("fixedlength")
                .parser(new FixedLengthParserBuilder().recordTerminator("\r\n"))//Esto para que ande en guindows

                .addRecord(dtoClass);
        factory.define(builder);

        BeanWriter out = factory.createWriter(recordName, new OutputStreamWriter(new FileOutputStream(archivo),
                "ISO-8859-1"));

        for (T dto : datosDeudores) {
            out.write(dto);
        }

        out.flush();
        out.close();

    }

    public void exportarDatosCSV(List<T> datosDeudores, File archivo) {

        StreamFactory factory = StreamFactory.newInstance();
        StreamBuilder builder = new StreamBuilder(recordName)
                .format("delimited")
                .addRecord(dtoClass);
        factory.define(builder);

        BeanWriter out = factory.createWriter(recordName, archivo);

        for (T dto : datosDeudores) {
            out.write(dto);
        }

        out.flush();
        out.close();

    }

}
