-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 21, 2020 at 09:04 PM
-- Server version: 5.6.47-cll-lve
-- PHP Version: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `anviinfitechs`
--

-- --------------------------------------------------------

--
-- Table structure for table `Approved_Plantation_Plan`
--

CREATE TABLE `Approved_Plantation_Plan` (
  `Unique_code` text NOT NULL,
  `Name` text NOT NULL,
  `Email` text NOT NULL,
  `Mobile` text NOT NULL,
  `Tree_name` text NOT NULL,
  `Month_Slot` text NOT NULL,
  `Grant_Status` text NOT NULL,
  `Date` date NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Approved_Plantation_Plan`
--

INSERT INTO `Approved_Plantation_Plan` (`Unique_code`, `Name`, `Email`, `Mobile`, `Tree_name`, `Month_Slot`, `Grant_Status`, `Date`) VALUES
('489022', 'Test User', 'test@gmail.com', '9999999999', 'Mango', '1', 'Yes', '2020-06-19'),;

-- --------------------------------------------------------

--
-- Table structure for table `Approve_Plant_Picture`
--

CREATE TABLE `Approve_Plant_Picture` (
  `Unique_code` text NOT NULL,
  `name` text NOT NULL,
  `email` text NOT NULL,
  `mobile` text NOT NULL,
  `longitude` text NOT NULL,
  `latitudes` text NOT NULL,
  `month` text NOT NULL,
  `tree_name` text NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `plant_picture` text NOT NULL,
  `approve_status` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Assign_Tree_Details`
--

CREATE TABLE `Assign_Tree_Details` (
  `name` text NOT NULL,
  `email` text NOT NULL,
  `mobile` text NOT NULL,
  `tree_count` text NOT NULL,
  `tree_name1` text CHARACTER SET utf8 COLLATE utf8_general_mysql500_ci NOT NULL,
  `tree_name2` text CHARACTER SET utf8 COLLATE utf8_general_mysql500_ci NOT NULL,
  `approval_date` date NOT NULL,
  `approval_time` time NOT NULL,
  `unique_code` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Assign_Tree_Details`
--

INSERT INTO `Assign_Tree_Details` (`name`, `email`, `mobile`, `tree_count`, `tree_name1`, `tree_name2`, `approval_date`, `approval_time`, `unique_code`) VALUES
('Test User', 'test@gmail.com', '99999999', '2', 'Mango', 'Chiku', '2020-06-19', '19:08:12', '489022');
-- --------------------------------------------------------

--
-- Table structure for table `haritkandharapp_user`
--

CREATE TABLE `haritkandharapp_user` (
  `unique_id` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `name` text COLLATE utf8_unicode_ci NOT NULL,
  `address` text COLLATE utf8_unicode_ci NOT NULL,
  `village` text COLLATE utf8_unicode_ci NOT NULL,
  `tehsil` text COLLATE utf8_unicode_ci NOT NULL,
  `district` text COLLATE utf8_unicode_ci NOT NULL,
  `pincode` text COLLATE utf8_unicode_ci NOT NULL,
  `survey_number` text COLLATE utf8_unicode_ci NOT NULL,
  `house_number` text COLLATE utf8_unicode_ci NOT NULL,
  `mobile` text COLLATE utf8_unicode_ci NOT NULL,
  `whatsapp_number` text COLLATE utf8_unicode_ci NOT NULL,
  `email` text COLLATE utf8_unicode_ci NOT NULL,
  `encrypted_password` text COLLATE utf8_unicode_ci NOT NULL,
  `verified` text COLLATE utf8_unicode_ci NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `otp` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `profile` text COLLATE utf8_unicode_ci NOT NULL,
  `admin_approved` text COLLATE utf8_unicode_ci NOT NULL,
  `block_status` text COLLATE utf8_unicode_ci NOT NULL,
  `agreement_status` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `haritkandharapp_user`
--

INSERT INTO `haritkandharapp_user` (`unique_id`, `name`, `address`, `village`, `tehsil`, `district`, `pincode`, `survey_number`, `house_number`, `mobile`, `whatsapp_number`, `email`, `encrypted_password`, `verified`, `created_at`, `otp`, `profile`, `admin_approved`, `block_status`, `agreement_status`) VALUES
('5eed6e5dc05749.38250805', 'Test User', 'test@gmail.com', 'kandhar', 'kandhar', 'nanded', '444444', 'Not Available', 'Not Available', '99999999', 'Not Available', 'test@gmail.com', '$2y$10$BMDNWqAPgJ/cZFwBoAjyiupUglbjc6xlCqW9pLF3wZLVkNkqp.gYK', 'verified', '2020-06-19 19:03:09', '822860', 'No', 'approved', 'unblocked', 'Agree');
-- --------------------------------------------------------

--
-- Table structure for table `haritkandharapp_user_admin`
--

CREATE TABLE `haritkandharapp_user_admin` (
  `unique_id` text NOT NULL,
  `name` text NOT NULL,
  `mobile` text NOT NULL,
  `email` text NOT NULL,
  `encrypted_password` text NOT NULL,
  `created_at` datetime NOT NULL,
  `profile` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `haritkandharapp_user_admin`
--

INSERT INTO `haritkandharapp_user_admin` (`unique_id`, `name`, `mobile`, `email`, `encrypted_password`, `created_at`, `profile`) VALUES
('5edd583567e6a8.72896805', 'Super Admin', '9999998888', 'superadmin@gmail.com', '$2y$10$VpTqcAc62Vi0EhmlF7UIW.ufHv6IjiF7pmL2ckupz80onIvHkEaHK', '2020-06-07 14:12:21', 'Admin.jpg'),
('5eddbd163ed8c0.76285716', 'Admin', '9999998888', 'admin1@gmail.com', '$2y$10$6NFdKTjOOmVGYbMWw18Y1ucLfJHVt0YdelrONsy/y5ZKrZG4mDTBi', '2020-06-07 21:22:46', 'Admin.jpg');-- --------------------------------------------------------

--
-- Table structure for table `User_Query`
--

CREATE TABLE `User_Query` (
  `name` text NOT NULL,
  `email` text NOT NULL,
  `mobile` text NOT NULL,
  `subject` text NOT NULL,
  `message` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `User_Query`
--

INSERT INTO `User_Query` (`name`, `email`, `mobile`, `subject`, `message`) VALUES
('Query Test', 'user_query@gmail.com', '9898989898', 'Congratulations', 'All the best. âœŒðŸ»');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
