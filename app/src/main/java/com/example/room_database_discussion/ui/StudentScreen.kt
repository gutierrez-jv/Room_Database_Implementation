package com.example.room_database_discussion.ui

import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.room_database_discussion.data.Student
import com.example.room_database_discussion.viewmodel.StudentViewModel

private val CampusBlue = Color(0xFF0757A5)
private val CampusNavy = Color(0xFF11233E)
private val CampusCream = Color(0xFFFFFDF7)
private val CampusSandstone = Color(0xFFF2E6CB)
private val BrickRed = Color(0xFFC95135)

@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    val students by viewModel.students.collectAsStateWithLifecycle()
    val nameError by viewModel.nameError.collectAsStateWithLifecycle()
    val emailError by viewModel.emailError.collectAsStateWithLifecycle()
    val courseError by viewModel.courseError.collectAsStateWithLifecycle()
    val ageError by viewModel.ageError.collectAsStateWithLifecycle()
    val studentCount by viewModel.studentCount.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var editStudent by remember { mutableStateOf<Student?>(null) }
    var searchText by remember { mutableStateOf("") }
    var selectedFilterCourse by remember { mutableStateOf("All Courses") }
    var courseMenuExpanded by remember { mutableStateOf(false) }
    val courses = listOf("All Courses", "BSCpE", "BSCS", "BSEMC")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CampusCream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Student Management",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = CampusBlue
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CampusSandstone),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (editStudent == null) "Add a student" else "Edit student",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CampusNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            viewModel.validateName(it)
                        },
                        label = { Text("Name") },
                        isError = nameError != null,
                        supportingText = { nameError?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            viewModel.validateEmail(it)
                        },
                        label = { Text("Email") },
                        isError = emailError != null,
                        supportingText = { emailError?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = course,
                        onValueChange = {
                            course = it
                            viewModel.validateCourse(it)
                        },
                        label = { Text("Course") },
                        isError = courseError != null,
                        supportingText = { courseError?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = age,
                        onValueChange = {
                            age = it
                            viewModel.validateAge(it)
                        },
                        label = { Text("Age") },
                        isError = ageError != null,
                        supportingText = { ageError?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val saved = editStudent?.let { selectedStudent ->
                                viewModel.updateStudent(
                                    selectedStudent.copy(
                                        name = name,
                                        email = email,
                                        course = course
                                    ),
                                    age
                                )
                            } ?: viewModel.addStudent(name, email, course, age)

                            if (saved) {
                                name = ""
                                email = ""
                                course = ""
                                age = ""
                                editStudent = null
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CampusBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (editStudent == null) "Add Student" else "Save Changes")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.searchStudents(it)
                    },
                    label = { Text("Search students") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(6.dp))
                Button(
                    onClick = { courseMenuExpanded = true },
                    modifier = Modifier.height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CampusBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("$selectedFilterCourse ▼")
                }
            }

            DropdownMenu(
                expanded = courseMenuExpanded,
                onDismissRequest = { courseMenuExpanded = false }
            ) {
                courses.forEach { courseOption ->
                    DropdownMenuItem(
                        text = { Text(courseOption) },
                        onClick = {
                            selectedFilterCourse = courseOption
                            courseMenuExpanded = false
                            viewModel.filterStudentsByCourse(courseOption)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Total Students: $studentCount",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = CampusNavy
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(students) { student ->
                    StudentItem(
                        student = student,
                        onEdit = {
                            editStudent = student
                            name = student.name
                            email = student.email
                            course = student.course
                            age = student.age.toString()
                        },
                        onDelete = { viewModel.deleteStudent(student) }
                    )
                }
            }
        }
    }
}

@Composable
fun StudentItem(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(BrickRed)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = student.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CampusNavy
                        )
                        Text(
                            text = student.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CampusNavy.copy(alpha = 0.7f)
                        )
                    }
                    Row {
                        OutlinedButton(
                            onClick = onEdit,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CampusBlue)
                        ) {
                            Text("Edit")
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        OutlinedButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BrickRed)
                        ) {
                            Text("Delete")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = CampusBlue.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = student.course,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            color = CampusBlue,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Text(
                        text = "Age: ${student.age}",
                        modifier = Modifier.padding(top = 5.dp),
                        color = CampusNavy.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
