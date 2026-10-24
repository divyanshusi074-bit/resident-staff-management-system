
package com.example.demo.controller;

import com.example.demo.entity.Resident;
import com.example.demo.entity.Staff;
import com.example.demo.service.ResidentService;
import com.example.demo.service.StaffService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

    @Controller
    public class ExportController {

        private final ResidentService residentService;
        private final StaffService staffService;

        public ExportController(
                ResidentService residentService,
                StaffService staffService) {
            this.residentService = residentService;
            this.staffService = staffService;
        }

        @GetMapping("/export/residents")
        public void exportResidents(HttpServletResponse response)
                throws IOException {

            List<Resident> residents = residentService.getAllResidents();

            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=residents.csv"
            );

            Writer writer = response.getWriter();

            // UTF-8 BOM helps Excel display text correctly.
            writer.write('\uFEFF');

            writeRow(writer,
                    "ID", "Name", "Email", "Phone", "Flat Number");

            for (Resident resident : residents) {
                writeRow(writer,
                        value(resident.getId()),
                        value(resident.getName()),
                        value(resident.getEmail()),
                        value(resident.getPhone()),
                        value(resident.getFlatNumber())
                );
            }

            writer.flush();
        }

        @GetMapping("/export/staff")
        public void exportStaff(HttpServletResponse response)
                throws IOException {

            List<Staff> staffList = staffService.getAllStaff();

            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=staff.csv"
            );

            Writer writer = response.getWriter();

            // UTF-8 BOM helps Excel display text correctly.
            writer.write('\uFEFF');

            writeRow(writer,
                    "ID", "Name", "Email", "Phone",
                    "Role", "Department", "Salary");

            for (Staff staff : staffList) {
                writeRow(writer,
                        value(staff.getId()),
                        value(staff.getName()),
                        value(staff.getEmail()),
                        value(staff.getPhone()),
                        value(staff.getRole()),
                        value(staff.getDepartment()),
                        value(staff.getSalary())
                );
            }

            writer.flush();
        }

        private void writeRow(Writer writer, String... values)
                throws IOException {

            for (int i = 0; i < values.length; i++) {
                if (i > 0) {
                    writer.write(',');
                }

                writer.write(escapeCsv(values[i]));
            }

            writer.write("\r\n");
        }

        private String value(Object value) {
            return value == null ? "" : value.toString();
        }

        private String escapeCsv(String value) {
            if (value == null) {
                value = "";
            }

            // Reduce CSV formula-injection risk in spreadsheet applications.
            String checked = value.stripLeading();

            if (!checked.isEmpty()
                    && "=+-@".indexOf(checked.charAt(0)) >= 0) {
                value = "'" + value;
            }

            // Escape quotes and wrap fields containing CSV special characters.
            value = value.replace("\"", "\"\"");

            if (value.contains(",")
                    || value.contains("\"")
                    || value.contains("\n")
                    || value.contains("\r")) {
                value = "\"" + value + "\"";
            }

            return value;
        }
    }


