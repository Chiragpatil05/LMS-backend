//package com.edulearn.config;
//
//import com.edulearn.model.User;
//import com.edulearn.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DatabaseInitializer implements CommandLineRunner {
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Create demo users if they don't exist
//        if (!userService.findByEmail("instructor@example.com").isPresent()) {
//            User instructor = new User();
//            instructor.setName("John Smith");
//            instructor.setEmail("instructor@example.com");
//            instructor.setPassword("password123");
//            instructor.setRole("instructor");
//            instructor.setAvatar("https://ui-avatars.com/api/?name=John+Smith&background=random");
//            userService.createUser(instructor);
//        }
//
//        if (!userService.findByEmail("student@example.com").isPresent()) {
//            User student = new User();
//            student.setName("Sarah Johnson");
//            student.setEmail("student@example.com");
//            student.setPassword("password123");
//            student.setRole("student");
//            student.setAvatar("https://ui-avatars.com/api/?name=Sarah+Johnson&background=random");
//            userService.createUser(student);
//        }
//    }
//}

package com.edulearn.config;

import com.edulearn.model.User;
import com.edulearn.model.Course;
import com.edulearn.model.CourseOutcome;
import com.edulearn.model.CoursePrerequisite;
import com.edulearn.model.Lesson;
import com.edulearn.service.UserService;
import com.edulearn.repository.CourseRepository;
import com.edulearn.repository.CourseOutcomeRepository;
import com.edulearn.repository.CoursePrerequisiteRepository;
import com.edulearn.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseOutcomeRepository outcomeRepository;

    @Autowired
    private CoursePrerequisiteRepository prerequisiteRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create demo users if they don't exist
        User instructor = null;
        User student = null;

        if (!userService.findByEmail("instructor@example.com").isPresent()) {
            instructor = new User();
            instructor.setName("John Smith");
            instructor.setEmail("instructor@example.com");
            instructor.setPassword("password123");
            instructor.setRole("instructor");
            instructor.setAvatar("https://ui-avatars.com/api/?name=John+Smith&background=random");
            instructor = userService.createUser(instructor);
        } else {
            instructor = userService.findByEmail("instructor@example.com").get();
        }

        if (!userService.findByEmail("student@example.com").isPresent()) {
            student = new User();
            student.setName("Sarah Johnson");
            student.setEmail("student@example.com");
            student.setPassword("password123");
            student.setRole("student");
            student.setAvatar("https://ui-avatars.com/api/?name=Sarah+Johnson&background=random");
            student = userService.createUser(student);
        } else {
            student = userService.findByEmail("student@example.com").get();
        }

        // Create demo courses if they don't exist
        if (courseRepository.count() == 0) {
            createDemoCourses(instructor);
        }
    }

    private void createDemoCourses(User instructor) {
        // Course 1: Web Development Fundamentals
        Course course1 = new Course();
        course1.setTitle("Web Development Fundamentals");
        course1.setDescription("Learn the core fundamentals of web development, including HTML, CSS, and JavaScript. This course is perfect for beginners who want to start their journey in web development.");
        course1.setThumbnail("https://images.pexels.com/photos/1181263/pexels-photo-1181263.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2");
        course1.setCategory("web-development");
        course1.setLevel("beginner");
        course1.setPrice(49.99);
        course1.setOriginalPrice(99.99);
        course1.setStatus("active");
        course1.setInstructor(instructor);
        course1.setRating(4.8);
        course1.setEnrolled(1250);
        course1.setCreatedAt(LocalDateTime.now());
        course1.setUpdatedAt(LocalDateTime.now());
        course1 = courseRepository.save(course1);

        // Add outcomes for course 1
        List<String> outcomes1 = Arrays.asList(
                "Build responsive websites using HTML and CSS",
                "Create interactive web pages with JavaScript",
                "Understand the fundamentals of web design",
                "Deploy websites to the internet"
        );
        for (String outcome : outcomes1) {
            CourseOutcome courseOutcome = new CourseOutcome();
            courseOutcome.setCourse(course1);
            courseOutcome.setOutcome(outcome);
            outcomeRepository.save(courseOutcome);
        }

        // Add prerequisites for course 1
        List<String> prerequisites1 = Arrays.asList(
                "Basic computer skills",
                "No prior programming experience required"
        );
        for (String prerequisite : prerequisites1) {
            CoursePrerequisite coursePrerequisite = new CoursePrerequisite();
            coursePrerequisite.setCourse(course1);
            coursePrerequisite.setPrerequisite(prerequisite);
            prerequisiteRepository.save(coursePrerequisite);
        }

        // Add lessons for course 1
        List<String[]> lessons1 = Arrays.asList(
                new String[]{"Introduction to HTML", "Learn the basics of HTML, including tags, elements, and document structure.", "45 min", "https://example.com/videos/html-intro"},
                new String[]{"CSS Fundamentals", "Understand how to style web pages using CSS selectors, properties, and values.", "50 min", "https://example.com/videos/css-basics"},
                new String[]{"JavaScript Basics", "Introduction to JavaScript programming for web development.", "60 min", "https://example.com/videos/js-basics"},
                new String[]{"Responsive Web Design", "Learn how to create websites that work well on all devices.", "55 min", "https://example.com/videos/responsive-design"}
        );
        for (String[] lessonData : lessons1) {
            Lesson lesson = new Lesson();
            lesson.setCourse(course1);
            lesson.setTitle(lessonData[0]);
            lesson.setDescription(lessonData[1]);
            lesson.setDuration(lessonData[2]);
            lesson.setVideoUrl(lessonData[3]);
            lessonRepository.save(lesson);
        }

        // Course 2: Advanced JavaScript Programming
        Course course2 = new Course();
        course2.setTitle("Advanced JavaScript Programming");
        course2.setDescription("Take your JavaScript skills to the next level with advanced concepts like closures, prototypes, async programming, and modern ES6+ features.");
        course2.setThumbnail("https://images.pexels.com/photos/270348/pexels-photo-270348.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2");
        course2.setCategory("web-development");
        course2.setLevel("intermediate");
        course2.setPrice(69.99);
        course2.setOriginalPrice(129.99);
        course2.setStatus("active");
        course2.setInstructor(instructor);
        course2.setRating(4.9);
        course2.setEnrolled(750);
        course2.setCreatedAt(LocalDateTime.now());
        course2.setUpdatedAt(LocalDateTime.now());
        course2 = courseRepository.save(course2);

        // Add outcomes for course 2
        List<String> outcomes2 = Arrays.asList(
                "Master advanced JavaScript concepts",
                "Write clean, efficient, and maintainable code",
                "Understand asynchronous programming",
                "Build complex applications with modern JavaScript"
        );
        for (String outcome : outcomes2) {
            CourseOutcome courseOutcome = new CourseOutcome();
            courseOutcome.setCourse(course2);
            courseOutcome.setOutcome(outcome);
            outcomeRepository.save(courseOutcome);
        }

        // Add prerequisites for course 2
        List<String> prerequisites2 = Arrays.asList(
                "Basic JavaScript knowledge",
                "Understanding of HTML and CSS",
                "Some programming experience"
        );
        for (String prerequisite : prerequisites2) {
            CoursePrerequisite coursePrerequisite = new CoursePrerequisite();
            coursePrerequisite.setCourse(course2);
            coursePrerequisite.setPrerequisite(prerequisite);
            prerequisiteRepository.save(coursePrerequisite);
        }

        // Add lessons for course 2
        List<String[]> lessons2 = Arrays.asList(
                new String[]{"JavaScript Scope and Closures", "Understanding lexical scope, execution context, and closures in JavaScript.", "65 min", "https://example.com/videos/js-closures"},
                new String[]{"Prototypes and Inheritance", "Learn about the prototype chain and object-oriented programming in JavaScript.", "70 min", "https://example.com/videos/js-prototypes"},
                new String[]{"Asynchronous JavaScript", "Master callbacks, promises, and async/await for handling asynchronous operations.", "80 min", "https://example.com/videos/js-async"},
                new String[]{"Modern JavaScript (ES6+)", "Explore the latest JavaScript features and syntax improvements.", "75 min", "https://example.com/videos/js-es6"}
        );
        for (String[] lessonData : lessons2) {
            Lesson lesson = new Lesson();
            lesson.setCourse(course2);
            lesson.setTitle(lessonData[0]);
            lesson.setDescription(lessonData[1]);
            lesson.setDuration(lessonData[2]);
            lesson.setVideoUrl(lessonData[3]);
            lessonRepository.save(lesson);
        }

        // Course 3: Mobile App Development with React Native
        Course course3 = new Course();
        course3.setTitle("Mobile App Development with React Native");
        course3.setDescription("Learn how to build native mobile applications for iOS and Android using React Native framework.");
        course3.setThumbnail("https://images.pexels.com/photos/238480/pexels-photo-238480.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2");
        course3.setCategory("mobile-development");
        course3.setLevel("intermediate");
        course3.setPrice(79.99);
        course3.setOriginalPrice(149.99);
        course3.setStatus("active");
        course3.setInstructor(instructor);
        course3.setRating(4.7);
        course3.setEnrolled(620);
        course3.setCreatedAt(LocalDateTime.now());
        course3.setUpdatedAt(LocalDateTime.now());
        course3 = courseRepository.save(course3);

        // Add outcomes for course 3
        List<String> outcomes3 = Arrays.asList(
                "Build cross-platform mobile apps with React Native",
                "Understand mobile app architecture and navigation",
                "Implement native device features",
                "Deploy apps to app stores"
        );
        for (String outcome : outcomes3) {
            CourseOutcome courseOutcome = new CourseOutcome();
            courseOutcome.setCourse(course3);
            courseOutcome.setOutcome(outcome);
            outcomeRepository.save(courseOutcome);
        }

        // Add prerequisites for course 3
        List<String> prerequisites3 = Arrays.asList(
                "JavaScript fundamentals",
                "Basic React knowledge",
                "Understanding of mobile app concepts"
        );
        for (String prerequisite : prerequisites3) {
            CoursePrerequisite coursePrerequisite = new CoursePrerequisite();
            coursePrerequisite.setCourse(course3);
            coursePrerequisite.setPrerequisite(prerequisite);
            prerequisiteRepository.save(coursePrerequisite);
        }

        // Add lessons for course 3
        List<String[]> lessons3 = Arrays.asList(
                new String[]{"Getting Started with React Native", "Setting up your development environment and creating your first React Native app.", "55 min", "https://example.com/videos/react-native-intro"},
                new String[]{"Components and Styling", "Learn about React Native components and how to style them.", "60 min", "https://example.com/videos/react-native-components"},
                new String[]{"Navigation and Routing", "Implement navigation between screens in your React Native app.", "65 min", "https://example.com/videos/react-native-navigation"},
                new String[]{"Native Device Features", "Access camera, location, and other native device capabilities.", "70 min", "https://example.com/videos/react-native-device-features"}
        );
        for (String[] lessonData : lessons3) {
            Lesson lesson = new Lesson();
            lesson.setCourse(course3);
            lesson.setTitle(lessonData[0]);
            lesson.setDescription(lessonData[1]);
            lesson.setDuration(lessonData[2]);
            lesson.setVideoUrl(lessonData[3]);
            lessonRepository.save(lesson);
        }
    }
}