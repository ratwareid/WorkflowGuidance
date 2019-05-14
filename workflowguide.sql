--
-- Database: `workflowguide`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `m_company`
--

CREATE TABLE `m_company` (
  `company_id` bigint(64) NOT NULL,
  `company_name` varchar(32) NOT NULL,
  `company_address` varchar(32) NOT NULL,
  `company_director` varchar(32) NOT NULL,
  `business_type` varchar(32) NOT NULL,
  `phone_number` varchar(32) NOT NULL,
  `website` varchar(40) NOT NULL,
  `incorporated_date` date NOT NULL,
  `company_img` mediumtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `m_company`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `m_user`
--

CREATE TABLE `m_user` (
  `user_id` bigint(64) NOT NULL,
  `name` varchar(40) NOT NULL,
  `address` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `company_id` bigint(64) NOT NULL,
  `active` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Indexes for table `m_company`
--
ALTER TABLE `m_company`
  ADD PRIMARY KEY (`company_id`);

--
-- Indexes for table `m_user`
--
ALTER TABLE `m_user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `m_company`
--
ALTER TABLE `m_company`
  MODIFY `company_id` bigint(64) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1557814207;
--
-- AUTO_INCREMENT for table `m_user`
--
ALTER TABLE `m_user`
  MODIFY `user_id` bigint(64) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
