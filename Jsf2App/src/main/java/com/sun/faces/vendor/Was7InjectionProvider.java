package com.sun.faces.vendor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.ibm.wsspi.webcontainer.annotation.AnnotationHelper;
import com.ibm.wsspi.webcontainer.annotation.AnnotationHelperManager;
import com.sun.faces.config.ConfigManager;
import com.sun.faces.spi.DiscoverableInjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import com.sun.faces.vendor.WebContainerInjectionProvider;

/**
 * InjectionProvider for JSF2 RI for WebSphere Application Server v7
 */
public class Was7InjectionProvider extends DiscoverableInjectionProvider {

    public static final String CLASS_NAME = Was7InjectionProvider.class
            .getName();
    private static final Logger LOGGER = Logger
            .getLogger(Was7InjectionProvider.CLASS_NAME);

    private AnnotationHelper annotationHelper;
    private AnnotationHelperManager annotationHelperManager;
    private WebContainerInjectionProvider lifecycleHelper;

    public Was7InjectionProvider() {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) currentInstance
                .getExternalContext().getContext();
        annotationHelperManager = AnnotationHelperManager
                .getInstance(servletContext);
        annotationHelper = annotationHelperManager.getAnnotationHelper();

        lifecycleHelper = new WebContainerInjectionProvider();
        addAnnotatedManagedBeans(currentInstance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inject(Object managedBean) throws InjectionProviderException {
        try {
            annotationHelper.inject(managedBean);
        } catch (Exception ie) {
            throw new InjectionProviderException(ie);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invokePostConstruct(Object managedBean)
            throws InjectionProviderException {
        try {
            lifecycleHelper.invokePostConstruct(managedBean);
        } catch (Exception ie) {
            throw new InjectionProviderException(ie);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invokePreDestroy(Object managedBean)
            throws InjectionProviderException {
        try {
            lifecycleHelper.invokePreDestroy(managedBean);
        } catch (Exception ie) {
            throw new InjectionProviderException(ie);
        }
    }

    private void addAnnotatedManagedBeans(FacesContext currentInstance) {
        // Target list of classes
        List<Class<?>> classesToScan = annotationHelper.getClassesToScan();
        // Getting list of classes annotaded with @ManagedBean
        Set<Class<?>> managedBeanClasses = ConfigManager.getAnnotatedClasses(
                currentInstance).get(ManagedBean.class);
        // Make a copy
        Set<Class<?>> classesToAdd = new HashSet<Class<?>>(managedBeanClasses);
        // Remove classes that already exist in list
        classesToAdd.removeAll(classesToScan);
        if (classesToAdd.isEmpty()) {
            LOGGER.log(Level.INFO, "No @ManagedBean classes to add");
        } else {
            LOGGER.log(Level.INFO,
                    "Adding the following @ManagedBean classes: {0}",
                    classesToAdd);
            // Addeding result to target list of classes
            classesToScan.addAll(classesToAdd);
        }
    }
}