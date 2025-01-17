/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Manager;

import DAO.Product.productDAO_1;
import Model.Product;
import Upload.EncForm;
import Upload.EncFormResult;
import Upload.UploadItem;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Admin
 */
@WebServlet(name = "EditProductControl", urlPatterns = {"/EditProductControl"})
public class EditProductControl extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        EncFormResult encFormData = new EncForm().getEncFormData(request, request.getServletContext());
        List<FileItem> fileItems = encFormData.getFileItems();
        HashMap<String, List<String>> formFields = encFormData.getFormFields();
        
//        String pid = request.getParameter("id");
//        String pname = request.getParameter("name");
//        String price = request.getParameter("price");
//        String number = request.getParameter("number");
//        String des = request.getParameter("des");
//        String category = request.getParameter("category");
        
        String pid = formFields.get("id").get(0);
        String pname = formFields.get("name").get(0);
        String price = formFields.get("price").get(0);
        String number = formFields.get("number").get(0);
        String des = formFields.get("des").get(0);
        String category = formFields.get("category").get(0);
        String[] color = formFields.get("color").toArray(new String[0]);
        String[] size = formFields.get("size").toArray(new String[0]);
    
        
        productDAO_1.editProduct(pname, price, number, des, category, pid);
        productDAO_1.deleteColorProduct(Integer.parseInt(pid));
        productDAO_1.deleteSizeProduct(Integer.parseInt(pid));
        productDAO_1.addColorProduct(Integer.parseInt(pid), color);
        productDAO_1.addSizeProduct(Integer.parseInt(pid), size);
         List<UploadItem> uploadItems = new ArrayList<>();
        
        fileItems.forEach(fi -> {
            UploadItem ui = new UploadItem(fi, pid + " (" + (fileItems.indexOf(fi)+1) + ')');
            uploadItems.add(ui);
        });

        String productImagePath = "images/productImage/" + pid;
  
        if(uploadItems.size() != 0) {
        Arrays.asList(new File(request.getServletContext().getRealPath(productImagePath)).listFiles()).forEach(f -> {
            System.out.println("delete "  + f.delete());
        });
        }
        System.out.println(uploadItems);
        
      List<String> uploadFile = EncForm.uploadFile(uploadItems.toArray(new UploadItem[]{}), productImagePath, request, request.getServletContext());
        
        System.out.println("uploaded: " + uploadFile);
        response.sendRedirect("ProductManagerControl");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
